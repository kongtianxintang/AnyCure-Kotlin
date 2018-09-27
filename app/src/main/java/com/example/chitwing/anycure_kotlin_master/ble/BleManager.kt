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
import android.os.Handler
import android.os.ParcelUuid
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.app.MyApp
import com.orhanobut.logger.Logger
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

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
     * handle
     * */
    private val mHandler: Handler by lazy {
        return@lazy Handler()
    }

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
                    Logger.d("蓝牙打开")
                    mStatusCallback?.bleStatus(CWBleStatus.ON)
                }
                BluetoothAdapter.STATE_OFF -> {
                    Logger.d("蓝牙关闭")
                    mStatusCallback?.bleStatus(CWBleStatus.OFF)
                }
            }
        }
    }

    /*蓝牙开始扫描*/
    fun startScan(){
        //todo:~~android 6.0需要请求定位权限
        val context = MyApp.getApp()
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        context.registerReceiver(mBluetoothReceiver,filter)

        if (mStatusCallback == null) {
            Logger.d("请设置回调")
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
            it.bluetoothLeScanner?.let {
                it.stopScan(mScannerCallback)
                mStatusCallback?.bleStatus(CWBleStatus.StopScan)
                mDevices.clear()
            }
        }
    }

    /**
     * 扫描回调
     * */
    private val mScannerCallback = object :ScanCallback(){
        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Logger.d("batch")
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
                    Logger.d("设备名称${it.device.name} 渠道号:$code")
                    val type = it.device.deviceType()
                    when(type){
                        CWDeviceType.Old -> {
//                            mDevices.add(it.device)
//                            mScanCallback!!.discoveryDevice(it.device,this@CWBleManager)
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
            Logger.d("Failed->$errorCode")
        }
    }

    /**
     * 去链接设备
     * */
    fun connect(device: BluetoothDevice){
        val gatt = device.connectGatt(MyApp.getApp(),false, mGattCallback)
        Logger.d("链接gatt->$gatt")
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
            val cw = mCWDevices.find { it.mGatt == gatt }
            cw?.gattRead?.handleData(characteristic?.value)
        }

        /**
         * 蓝牙链接状态
        * */
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if(status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothGatt.STATE_CONNECTED) {
                val device = mCWDevices.find { it.mDevice.address == gatt?.device?.address }
                if (device != null){//重新链接的
                    Logger.d("重新链接的 gatt->$gatt")
                    device.mGatt = gatt
                    gatt?.discoverServices()
                    device.setDeviceConnect(true)
                }else{//新链接
                    gatt?.let {
                        val job = launch {
                            /**保存自定义的外接设备*/
                            val cw = CWDevice(it.device,it)
                            mCWDevices.add(cw)
                            mStatusCallback?.onCreateCWDevice(cw)
                            mStatusCallback?.bleStatus(CWBleStatus.Connect)
                            Logger.d("去发现服务->延时1600ms 前")
                            delay(1600)
                            it.discoverServices()
                            Logger.d("去发现服务->延时1600ms 后")
                        }
                        job.start()
                    }
                }
            }else {
                if (newState == BluetoothGatt.STATE_DISCONNECTED){
                    Logger.d("断开链接")
                    val device = mCWDevices.find {  it.mDevice.address == gatt?.device?.address }
                    device?.let {
                        it.setDeviceConnect(false)
                        if (it.isAutoDisconnect){
                            gatt?.close()
                        }else{
                            gatt?.disconnect()
                            val isReConnect = gatt?.connect()
                            Logger.d("重连->$isReConnect 断开链接gatt对象->$gatt")
                        }
                    }
                    mStatusCallback?.bleStatus(CWBleStatus.Disconnect)
                }
                Logger.d("蓝牙连接未知状态$status new->$newState")
            }
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
                    Logger.d("desc写入:$isWrite desc的uuid:${it.uuid}")
                }
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)
            mStatusCallback?.bleStatus(CWBleStatus.Able)
            val cw = mCWDevices.find { it.mGatt == gatt }
            cw?.let {
                if (!it.isAutoDisconnect){
                    Logger.d("查询通信编号")
                    cw.queryCommunicationSerialNumber()
                }
            }
        }

        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorRead(gatt, descriptor, status)
            Logger.d("descriptor读取$status")
        }


        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
            Logger.d("信号量:$rssi")
        }

    }


    private fun checkBluetoothSupport(context: Context,bluetoothAdapter: BluetoothAdapter?): Boolean {
        if (bluetoothAdapter == null) {
            return false
        }
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false
        }
        return true
    }

}