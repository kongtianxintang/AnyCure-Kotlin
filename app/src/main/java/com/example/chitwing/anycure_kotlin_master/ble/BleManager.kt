package com.example.chitwing.anycure_kotlin_master.ble

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.ParcelUuid
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.app.MyApp

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/7
 * QQ/Tel/Mail:383118832
 * Description:管理蓝牙相关类
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
object  CWBleManager {

    private val tag = "蓝牙管理类"
    /**
     * 基础配置:1.渠道号 2.可用性
     * */
    val configure = CWConfigure()
    /**蓝牙状态 可否使用*/
    private var mStatusCallback:CWBleStatusInterface? = null
    /**
     * 扫描发现到的设备
     * */
    private var mScanCallback:CWScanCallback? = null

    /**蓝牙管理*/
    private var mBleManager:BluetoothManager? = null
    /** 蓝牙适配器 */
    private var mBleAdapter:BluetoothAdapter? = null
    /**
    * 保存外接设备类
    * */
    val mCWDevices = mutableListOf<CWDevice>()
    private val mDevices = mutableListOf<BluetoothDevice>()
    /**
     *设置蓝牙状态回调
     */
    fun setStatusCallback(statusCallback:CWBleStatusInterface) {
        mStatusCallback = null
        mStatusCallback = statusCallback
    }
    /**
     * 设置发现设备回调
     * */
    fun setScanCallback(callback: CWScanCallback){
        mScanCallback = null
        mScanCallback = callback
    }

    /**
    * 蓝牙状态监听
    * */
    private val mBluetoothReceiver = object :BroadcastReceiver (){
        override fun onReceive(context: Context, intent: Intent) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)

            when (state) {
                BluetoothAdapter.STATE_ON -> {
                    Log.d(tag,"蓝牙打开")
                }
                BluetoothAdapter.STATE_OFF -> {
                    Log.d(tag,"蓝牙关闭")
                }
            }
        }
    }

    /*蓝牙开始扫描*/
    fun startScan(){
        val context = MyApp.getApp()
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        context.registerReceiver(mBluetoothReceiver,filter)

        if (mStatusCallback == null) {
            Log.d(tag,"请设置回调")
            return
        }
        if (mBleManager == null){
            mBleManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        }
        mBleAdapter = mBleManager!!.adapter

        if (!checkBluetoothSupport(context,mBleAdapter)){
            mStatusCallback!!.bleStatus(CWBleStatus.DisSupport)
            return
        }

        if (!mBleAdapter!!.isEnabled){
            mStatusCallback!!.bleStatus(CWBleStatus.Disable)
            return
        }

        mDevices.clear()
        scanDevice()
    }


    /**
     * 开始扫描设备
     * */
    private fun scanDevice(){
        val scanner = mBleAdapter!!.bluetoothLeScanner
        val pUUID = ParcelUuid(CWGattAttributes.CW_SERVICE_UUID)
        val tFilter = ScanFilter.Builder().setServiceUuid(pUUID).build()
        val settings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()
        scanner.startScan(listOf(tFilter),settings, mScannerCallback)
        mStatusCallback?.bleStatus(CWBleStatus.BeginScan)
    }

    /**
     * 停止扫描设备
     * */
    fun stopScanDevice(){
        mBleAdapter?.let {
            val scanner = it.bluetoothLeScanner
            scanner.stopScan(mScannerCallback)
            mStatusCallback?.bleStatus(CWBleStatus.StopScan)
            mDevices.clear()
        }
    }

    /**
     * 扫描回调
     * */
    private val mScannerCallback = object :ScanCallback(){
        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Log.d(tag,"batch")
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            result?.let {
                if (mScanCallback != null){
                    if (mDevices.contains(it.device)){
                        return@let
                    }
                    /**
                     * 判断设备新旧
                     * 旧的设备都可以链接
                     * 新的设备则需要判断 client 与 设备为同一个渠道
                     * */
                    val code = it.device.channelCode()
                    Log.d(tag,"设备名称${it.device.name} 渠道号:$code")
                    val type = it.device.deviceType()
                    when(type){
                        CWDeviceType.Old -> {
                            mDevices.add(it.device)
                            mScanCallback!!.discoveryDevice(it.device,this@CWBleManager)
                        }
                        else -> {
                            when(configure.channel){//如果为自己有渠道 则都可以链接
                                CWChannel.ChitWing,CWChannel.ALL -> {
                                    mDevices.add(it.device)
                                    mScanCallback!!.discoveryDevice(it.device,this@CWBleManager)
                                }
                                else -> {
                                    if (code == configure.channel.SHORT_NUM_CODE){
                                        mDevices.add(it.device)
                                        mScanCallback!!.discoveryDevice(it.device,this@CWBleManager)
                                    }
                                }
                            }
                        }
                        }
                    }
                }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d(tag,"Failed->$errorCode")
        }
    }

    /**
     * 去链接设备
     * */
    fun connect(device: BluetoothDevice){
        val gatt = device.connectGatt(MyApp.getApp(),false, mGattCallback)
        Log.d(tag,"链接gatt->$gatt")
    }




    /**
     * 数据交互回调
     * */
    private val mGattCallback = object :BluetoothGattCallback (){

        /**
         * 特征改变
         * 获取到外接设备发回到值
         * */
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)
            Log.d(tag,"特征改变")
            val cw = mCWDevices.find { it.mGatt == gatt }
            cw?.gattRead?.handleData(characteristic?.value)
        }

        /**
         * 蓝牙链接状态
        * */
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
//            when(status){
//                BluetoothGatt.GATT_SUCCESS -> {
                    when(newState){
                        BluetoothGatt.STATE_CONNECTED ->{
                            val device = mCWDevices.find { it.mDevice.address == gatt?.device?.address }
                            if (device != null){//重新链接的
                                Log.d(tag,"重新链接的 gatt->$gatt")
                                device.mGatt = gatt
                                gatt?.discoverServices()
                            }else{//新链接
                                gatt?.let {
                                    it.discoverServices()
                                    /**保存自定义的外接设备*/
                                    val cw = CWDevice(it.device,it)
                                    mCWDevices.add(cw)
                                    mStatusCallback?.onCreateCWDevice(cw)
                                    mStatusCallback?.bleStatus(CWBleStatus.Connect)
                                }
                            }
                        }
                        BluetoothGatt.STATE_DISCONNECTED -> {
                            Log.d(tag,"断开链接")
                            val device = mCWDevices.find {  it.mDevice.address == gatt?.device?.address }
                            device?.let {
                                if (it.isAutoDisconnect){
                                    gatt?.close()
                                }else{
                                    gatt?.disconnect()
                                    val isReConnect = gatt?.connect()
                                    Log.d(tag,"重连->$isReConnect 断开链接gatt对象->$gatt")
                                }
                            }
                            mStatusCallback?.bleStatus(CWBleStatus.Disconnect)
                        }
                        else -> {

                        }
                    }
//                }
//                else -> {
//                    Log.d(tag,"gatt不知道啥状态$status")
//                    gatt?.close()
//                }
//            }
        }

        /**
         * 发现服务
         * */
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            mStatusCallback?.bleStatus(CWBleStatus.Discover)
            gatt?.let {
                val service = it.getService(CWGattAttributes.CW_SERVICE_UUID)
                val character = service.getCharacteristic(CWGattAttributes.CW_notifyUUID)
                if (character != null) {
                    it.setCharacteristicNotification(character, true)
                }

                character.descriptors.forEach {
                    it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    val isWrite = gatt.writeDescriptor(it)
                    Log.d(tag, "desc写入:$isWrite desc的uuid:${it.uuid}")
                }
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)
            Log.d(tag,"descriptor写入$status")
            mStatusCallback?.bleStatus(CWBleStatus.Able)
            val cw = mCWDevices.find { it.mGatt == gatt }
            cw?.let {
                Log.d(tag,"查询设备状态")
                cw.gattWrite.cwBleWriteDeviceStatusQuery()
            }
        }

        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorRead(gatt, descriptor, status)
            Log.d(tag,"descriptor读取$status")
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
            Log.d(tag,"特征值读取:${characteristic?.value}")
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            Log.d(tag,"特征值写入:${characteristic?.value}")
            val cw = mCWDevices.find { it.mGatt == gatt }
            characteristic?.value?.let {
                cw?.readCharacteristicData(it)
            }
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
            Log.d(tag,"信号量:$rssi")
        }

    }


    private val bleAdapterCallback = object :BluetoothAdapter.LeScanCallback {
        override fun onLeScan(device: BluetoothDevice?, rssi: Int, scanRecord: ByteArray?) {
            Log.d(tag,"扫描蓝牙:${device!!.address} rssi:$rssi")
        }
    }



    private fun checkBluetoothSupport(context: Context,bluetoothAdapter: BluetoothAdapter?): Boolean {
        if (bluetoothAdapter == null) {
            Log.d(tag, "Bluetooth is not supported")
            return false
        }
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.d(tag, "Bluetooth LE is not supported")
            return false
        }
        return true
    }

}