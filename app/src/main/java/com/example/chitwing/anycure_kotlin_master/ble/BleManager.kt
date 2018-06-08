package com.example.chitwing.anycure_kotlin_master.ble

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
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
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.app.MyApp
import java.util.*

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
    /**蓝牙状态 可否使用*/
    private var mStatusCallback:CWBleStatusInterface? = null
    /**蓝牙管理*/
    private var mBleManager:BluetoothManager? = null
    /** 蓝牙适配器 */
    private var mBleAdapter:BluetoothAdapter? = null
    /**
    * 保存外接设备类
    * */
    val mDevices = mutableListOf<CWDevice>()

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

//        mBleAdapter!!.startLeScan(bleAdapterCallback)
        scanDevice()
    }


    private fun scanDevice(){
        val scanner = mBleAdapter!!.bluetoothLeScanner
        val uuid = UUID.fromString(CWGattAttributes.sUUID)
        val pUUID = ParcelUuid(uuid)
        val tFilter = ScanFilter.Builder().setServiceUuid(pUUID).build()
        val settings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()
        scanner.startScan(listOf(tFilter),settings, mScannerCallback)
    }

    private fun stopScanDevice(){
        val scanner = mBleAdapter!!.bluetoothLeScanner
        scanner.stopScan(mScannerCallback)
    }

    private val mScannerCallback = object :ScanCallback(){
        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Log.d(tag,"batch")
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            result?.let {
                val cw = CWDevice(it.device)
                if (!mDevices.contains(cw)){
                    mDevices.add(cw)
                    Log.e(tag,"设备${cw.device.name}")
                    stopScanDevice()
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d(tag,"Failed")
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