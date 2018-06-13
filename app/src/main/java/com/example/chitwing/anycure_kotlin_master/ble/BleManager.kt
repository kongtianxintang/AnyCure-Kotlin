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
    /**蓝牙管理*/
    private var mBleManager:BluetoothManager? = null
    /** 蓝牙适配器 */
    private var mBleAdapter:BluetoothAdapter? = null
    /**
    * 保存外接设备类
    * */
    val mCWDevices = mutableListOf<CWDevice>()
    val mDevices = mutableListOf<BluetoothDevice>()
    /**
     *
     */
    fun setStatusCallback(statusCallback:CWBleStatusInterface) {
        mStatusCallback = statusCallback
    }

    /**
    * 蓝牙状态监听
    * */
    private val mBluetoothReceiver = object :BroadcastReceiver (){
        override fun onReceive(context: Context, intent: Intent) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)

            when (state) {
                BluetoothAdapter.STATE_ON -> {
                    Log.e(tag,"蓝牙打开")
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
            Log.e(tag,"请设置回调")
            return
        }

        mBleManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBleAdapter = mBleManager!!.adapter

        if (!checkBluetoothSupport(context,mBleAdapter)){
            mStatusCallback!!.bleStatus(CWBleStatus.DisSupport)
            return
        }

        if (!mBleAdapter!!.isEnabled){
            mStatusCallback!!.bleStatus(CWBleStatus.Disable)
            return
        }

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
    }

    /**
     * 停止扫描设备
     * */
    private fun stopScanDevice(){
        val scanner = mBleAdapter!!.bluetoothLeScanner
        scanner.stopScan(mScannerCallback)
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
                if (!mDevices.contains(it.device)){
                    if (it.device.name.contains("4B7B")){
                        mDevices.add(it.device)
                        it.device.connectGatt(MyApp.getApp(),false,mGattCallback)
                        stopScanDevice()
                        Log.e(tag,"设备${it.device.name}")
                    }
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d(tag,"Failed")
        }
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
            cw?.let {
                it.gattRead.handleData(characteristic?.value)
            }
        }

        /**
         * 蓝牙链接状态
        * */
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            when(status){
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.d(tag,"gatt处理完成")
                    when(newState){
                        BluetoothGatt.STATE_CONNECTED ->{
                            Log.d(tag,"链接成功2")
                            gatt?.let {
                                it.discoverServices()
                                /**保存自定义的外接设备*/
                                val cw = CWDevice(it.device,it)
                                mCWDevices.add(cw)
                            }
                        }
                        BluetoothGatt.STATE_DISCONNECTED -> {
                            Log.d(tag,"断开链接")
                            val e = mCWDevices.find {  it.mGatt == gatt }
                            e?.let {
                                it.removeSelf()
                                mDevices.remove(it.mDevice)
                                gatt?.close()
                            }

                        }
                        else -> {

                        }
                    }
                }
                else -> {
                    Log.d(tag,"gatt不知道啥状态$status")
                    gatt?.close()
                }
            }
        }

        /**
         * 发现服务
         * */
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            gatt?.let {
                it.services.forEach {
                    Log.d(tag,"服务-${it.uuid}")
                }
                val service = it.getService(CWGattAttributes.CW_SERVICE_UUID)
                val character = service.getCharacteristic(CWGattAttributes.CW_notifyUUID)
                Log.d(tag,"服务:${service.uuid}")
                Log.d(tag,"特征值:${character.uuid}")
                it.setCharacteristicNotification(character!!,true)

                character.descriptors.forEach {
                    it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    val isWrite = gatt.writeDescriptor(it)
                    Log.d(tag,"desc写入:$isWrite desc的uuid:${it.uuid}")
                }
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)
            Log.d(tag,"descriptor写入$status")
            val cw = mCWDevices.find { it.mGatt == gatt }
            cw?.writeData()
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
            Log.e(tag,"扫描蓝牙:${device!!.address} rssi:$rssi")
        }
    }



    private fun checkBluetoothSupport(context: Context,bluetoothAdapter: BluetoothAdapter?): Boolean {
        if (bluetoothAdapter == null) {
            Log.e(tag, "Bluetooth is not supported")
            return false
        }
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.e(tag, "Bluetooth LE is not supported")
            return false
        }
        return true
    }

}