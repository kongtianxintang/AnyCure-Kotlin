package com.example.chitwing.anycure_kotlin_master.fragment.otCure

import android.util.Log
import com.example.chitwing.anycure_kotlin_master.ble.CWDevice
import com.example.chitwing.anycure_kotlin_master.ble.CWDeviceInterface
import com.example.chitwing.anycure_kotlin_master.ble.CWDeviceStatusInterface
import com.example.chitwing.anycure_kotlin_master.dialog.CWDialog
import com.example.chitwing.anycure_kotlin_master.dialog.CWDialogInterface
import com.example.chitwing.anycure_kotlin_master.dialog.CWDialogType
import com.orhanobut.logger.Logger

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/8/15
 * QQ/Tel/Mail:383118832
 * Description:数据提供类
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class OtCureProvider(private val fm:OtCureFragment) {

    /**
     * 接口
     * */
    val callback = object : CWDeviceInterface {
        override fun cureEndEvent(index: Int,item: CWDevice) {
            Logger.d("cureEndEvent")
            fm.endStatus()
            fm.currentMinusDevice(index)
        }

        override fun cureStartEvent(item: CWDevice) {
            Logger.d("cureStartEvent")
            fm.playStatus()
            item.selectDevice(true)
            clearDialogs()
        }

        override fun cureStopEvent(item: CWDevice) {
            Logger.d("cureStopEvent")
            fm.stopStatus()
        }

        override fun transferPlayDuration(value: Int, item: CWDevice) {
            fm.setPlayDuration(value)
        }

        override fun deviceCloseEvent(index: Int,item: CWDevice) {
            Logger.d("deviceCloseEvent")
            fm.endStatus()
            fm.currentMinusDevice(index)
        }

        override fun deviceConnect(flag: Boolean, item: CWDevice) {
            Logger.d("deviceConnect")
            fm.setConnect(flag)
        }

        override fun prepareComplete(item: CWDevice) {
            Logger.d("prepareComplete")
            //todo~~准备完成,此页面未用到
        }

        override fun transferMainElectrodeNotify(value: Int, item: CWDevice) {
            Logger.d("transferMainElectrodeNotify")
            when(value){
                0 -> pushDialog("电极贴脱落,请正常贴于患处")
                30 -> pushDialog("电极片贴合异常,请正常贴于患处")
                else -> {}
            }
        }

        override fun transferIntensity(value: Int, item: CWDevice) {
            fm.setIntensityNum(value)
        }

        override fun transferMainElectrodeQuery(value: Int, item: CWDevice) {
            Logger.d("transferMainElectrodeQuery")
            when (value){
                0 -> pushDialog("电极贴脱落,请正常贴于患处")
                30 -> pushDialog("电极片贴合异常,请正常贴于患处")
                else -> {
                    item.startCureAction()
                }
            }
        }

        override fun transferPower(value: Int, item: CWDevice) {
            Logger.d("transferPower->$value")
            fm.setPowerIcon(value)
        }

        override fun prepareFail(error: String, item: CWDevice) {
            Logger.d("prepareFail")
            //todo:准备失败～～此页面未用到
            pushDialog(error)
        }

    }

    val statusCallback = object : CWDeviceStatusInterface {
        override fun transferDeviceClose(index: Int, item: CWDevice) {
            fm.minusDevice()
        }

        override fun transferDevicePlayComplete(index: Int, item: CWDevice) {
            fm.minusDevice()
        }
    }

    /**
     * 弹出错误提示
     * */
    private val mDialogs = mutableListOf<CWDialog>()
    private fun pushDialog(error: String){
        clearDialogs()
        val dialog = CWDialog.Builder().setType(CWDialogType.Hint).setTitle("错误提示").setDesc(error).create()
        dialog.setCallback(dialogInterface)
        dialog.show(fm.activity?.fragmentManager,"ot_cure_error")
        mDialogs.add(dialog)
    }

    private val dialogInterface = object : CWDialogInterface {
        override fun onClickButton(flag: Boolean, item: CWDialog) {
            item.dismiss()
            mDialogs.remove(item)
        }
    }
    /**
     * 清除dialog
     * */
    private fun clearDialogs(){
        mDialogs.forEach {
            it.dismiss()
            mDialogs.remove(it)
        }
    }
}