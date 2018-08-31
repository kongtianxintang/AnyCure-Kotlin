package com.example.chitwing.anycure_kotlin_master.activity.search

import android.bluetooth.BluetoothDevice
import android.os.Build
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.base.CWBaseProvider
import com.example.chitwing.anycure_kotlin_master.ble.*
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.model.BindDevice
import com.example.chitwing.anycure_kotlin_master.unit.showToast
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.jar.Manifest

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/21
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWSearchProvider(private val context: SearchActivity) :CWBaseProvider(context){

    /**
     * 是否在扫描
     * */
    private var mIsScan:Boolean = true
    private var job: Job? = null
    private val mBinds = mutableListOf<BindDevice>()

    override fun fetchDataSource() {

        val finds = DBHelper.findAll(BindDevice ::class.java)
        finds?.let {
            mBinds.addAll(it)
        }


        CWBleManager.setStatusCallback(bleStatusCallback)

        CWBleManager.setScanCallback(object : CWScanCallback{
            override fun discoveryDevice(item: BluetoothDevice, manager: CWBleManager) {
                if (!context.mDataSet.contains(item)){
                    val cw = mBinds.find { it.mac == item.address }
                    if (cw == null){
                        context.mDataSet.add(item)
                        context.mAdapter!!.notifyDataSetChanged()
                    }
                }
            }
        })

        startScan()
    }


    private val bleStatusCallback = object : CWBleStatusInterface{
        override fun bleStatus(arg: CWBleStatus) {
            when(arg){
                CWBleStatus.Disable -> {
                    context.showToast("设备蓝牙不可用，请打开蓝牙")
                }

                CWBleStatus.DisSupport -> {
                    context.showToast("设备不支持蓝牙")
                }

                CWBleStatus.Support -> {

                }

                CWBleStatus.Able -> {

                }

                CWBleStatus.Discover -> {

                }

                CWBleStatus.BeginScan -> {

                }

                CWBleStatus.StopScan -> {

                }

                CWBleStatus.ON -> {

                }

                CWBleStatus.OFF -> {

                }
            }
        }

        override fun onCreateCWDevice(arg: CWDevice) {

        }
    }

    private fun startScan(){
        CWBleManager.startScan()
        stopScan(15000)
    }

    fun bindSpecifyDevice(item:BluetoothDevice){
        val bond = item.createBond()
        if (bond){
            val temp = BindDevice()
            temp.mac = item.address

            DBHelper.insert(temp,BindDevice::class.java)
            context.finish()
        }
    }


    private fun stopScan(time:Int){
        if (mIsScan){
            job = launch(CommonPool) {
                delay(time)
                finish()
            }
            job!!.start()
        }
    }

    fun finish(){
        if (mIsScan){
            CWBleManager.stopScanDevice()
            job!!.cancel()
            job = null
        }
        mIsScan = false
    }

}