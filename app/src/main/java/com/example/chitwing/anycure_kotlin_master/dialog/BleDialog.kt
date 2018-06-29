package com.example.chitwing.anycure_kotlin_master.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.FragmentManager
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.ble.*
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.model.BindDevice
import com.example.chitwing.anycure_kotlin_master.model.Recipe

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/28
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class BleDialog : DialogFragment() {

    /**
     *
     * */
    private var mDialog:AlertDialog? = null
    /**
     * 处方
     * */
    private var mRecipe:Recipe? = null
    fun setRecipe(m:Recipe){
        this.mRecipe = m
    }

    /**
     * 回调接口
     * */
    private var mCallback:BleDialogInterface? = null
    fun setCallback(m:BleDialogInterface){
        this.mCallback = m
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dia = dialog
        Log.e(tag,"dia->$dia")
    }

    /**
     * 创建系统的dialog
     * */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity!!)

        builder.setCancelable(true)

        builder.setTitle("链接蓝牙")

        builder.setMessage("请打开蓝牙~")

        builder.setPositiveButton("确定", object  :DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }
        })

        builder.setNegativeButton("取消",object  :DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }
        })
        Log.e(tag,"onCreateDialog")

        mDialog = builder.create()

        return mDialog!!
    }


//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val v = inflater.inflate(R.layout.ble_dialog, container, false)
//
//        return v
//    }

    /**
     * 显示dialog
     * */
    fun showBleDialog(m:android.support.v4.app.FragmentManager){

        show(m,"BleDialog")
        CWBleManager.setScanCallback(mScanCallback)
        CWBleManager.setStatusCallback(mBleStatusCallback)
        CWBleManager.startScan()
    }

    /**
     * 扫描回调
     * */
    private val mScanCallback = object :CWScanCallback {
        override fun discoveryDevice(item: BluetoothDevice, manager: CWBleManager) {
            val device = mSaveDevices?.find { it.mac == item.address }
            device?.let {
                manager.stopScanDevice()
                manager.connect(item)
            }
        }
    }

    /**
     * 链接状态回调
     * */
    private val mBleStatusCallback = object :CWBleStatusInterface {
        override fun bleStatus(arg: CWBleStatus) {

            activity?.runOnUiThread {
                mDialog?.setMessage(arg.desc)
            }


            when(arg){
                CWBleStatus.Connect -> {
                    Log.e(tag,"已经连上了～～")
                    mCallback?.connectDevice()
                    onDismiss(null)
                }
                else -> {
                    Log.e(tag,arg.desc)
                }
            }
        }

        override fun onCreateCWDevice(arg: CWDevice) {
            arg.recipe = mRecipe
        }
    }


    /**
     * 保存在本地的设备地址
     * */
    private val mSaveDevices by lazy {
        return@lazy DBHelper.findAll(BindDevice ::class.java)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        CWBleManager.stopScanDevice()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCallback = null
        mDialog = null
        Log.e("dialog","销毁")
    }



}