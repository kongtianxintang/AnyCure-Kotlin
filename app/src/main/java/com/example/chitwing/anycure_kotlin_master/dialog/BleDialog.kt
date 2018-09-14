package com.example.chitwing.anycure_kotlin_master.dialog

import android.bluetooth.BluetoothDevice
import android.content.DialogInterface
import android.os.Bundle
import android.app.DialogFragment
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.ble.*
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.model.BindDevice
import com.example.chitwing.anycure_kotlin_master.model.Recipe
import java.util.*

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

    /**
     * 描述label
     * */
    private lateinit var mDesc: TextView
    private lateinit var mTitle: TextView
    private lateinit var mButton: Button
    private var mTimer: Timer? = null
    private var mTimerTask: TimerTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Dialog_MinWidth)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.ble_dialog,container)
        mDesc = view.findViewById(R.id.ble_dialog_desc)
        mTitle = view.findViewById(R.id.ble_dialog_title)
        mButton = view.findViewById(R.id.ble_dialog_cancel_button)
        configureSubviews()
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        CWBleManager.setScanCallback(mScanCallback)
        CWBleManager.setStatusCallback(mBleStatusCallback)
        CWBleManager.startScan()
        resumeTimer()
    }

    private fun configureSubviews(){
        mTitle.text = "提示"
        mButton.setOnClickListener {
            dismiss()
            mCallback?.onClickButton(false)
        }
    }

    /**
     * 扫描回调
     * */
    private val mScanCallback = object :CWScanCallback {
        override fun discoveryDevice(item: BluetoothDevice, manager: CWBleManager) {
            val device = mSaveDevices?.find { it.mac == item.address }
            device?.let {
                if (mDevice != null){
                    return@let
                }
                mDevice = item
                manager.connect(item)
                manager.stopScanDevice()
            }
        }
    }

    /**
     * 链接状态回调
     * */
    private val mBleStatusCallback = object :CWBleStatusInterface {
        override fun bleStatus(arg: CWBleStatus) {

            activity?.runOnUiThread {
                mDesc.text = arg.desc
            }


            when(arg){
                CWBleStatus.Able -> {
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

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val dm = DisplayMetrics()
            activity!!.windowManager!!.defaultDisplay.getMetrics(dm)
            it.window.setLayout((dm.widthPixels * 0.75).toInt(),ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    /**
     * 保存在本地的设备地址
     * */
    private val mSaveDevices by lazy {
        return@lazy DBHelper.findAll(BindDevice ::class.java)
    }
    /**
     * 搜索到的设备
     * */
    private var mDevice: BluetoothDevice? = null

    private fun scanNullDevice(){
        activity?.runOnUiThread {
            mDesc.text = "未找到控制器,请打开控制器"
        }
    }

    private fun resumeTimer(){
        var count = 30
        mTimer = Timer()
        mTimerTask = object :TimerTask(){
            override fun run() {
                count -= 1
                if (count <= 0){
                    deInitTimer()
                    scanNullDevice()
                }
            }
        }
        mTimer!!.schedule(mTimerTask!!,Date(),1000)
    }

    private fun deInitTimer(){
        mTimer?.cancel()
        mTimer = null
        mTimerTask?.cancel()
        mTimerTask = null
    }


    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        CWBleManager.stopScanDevice()
        resumeTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCallback = null
        mRecipe = null
        mDevice = null
    }



}