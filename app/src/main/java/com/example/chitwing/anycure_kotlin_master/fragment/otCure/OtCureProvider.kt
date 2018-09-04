package com.example.chitwing.anycure_kotlin_master.fragment.otCure

import android.util.Log
import com.example.chitwing.anycure_kotlin_master.ble.CWDevice
import com.example.chitwing.anycure_kotlin_master.ble.CWDeviceInterface
import com.example.chitwing.anycure_kotlin_master.ble.CWDeviceStatusInterface

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
    private val tag = "OtCureProvider"

    /**
     * 接口
     * */
    val callback = object : CWDeviceInterface {
        override fun cureEndEvent(index: Int,item: CWDevice) {
            Log.e(tag,"cureEndEvent")
            fm.endStatus()
            fm.currentMinusDevice(index)
        }

        override fun cureStartEvent(item: CWDevice) {
            Log.e(tag,"cureStartEvent")
            fm.playStatus()
            item.selectDevice(true)
        }

        override fun cureStopEvent(item: CWDevice) {
            Log.e(tag,"cureStopEvent")
            fm.stopStatus()
        }

        override fun transferPlayDuration(value: Int, item: CWDevice) {
            fm.setPlayDuration(value)
        }

        override fun deviceCloseEvent(index: Int,item: CWDevice) {
            Log.e(tag,"deviceCloseEvent")
            fm.endStatus()
            fm.currentMinusDevice(index)
        }

        override fun deviceConnect(flag: Boolean, item: CWDevice) {
            Log.e(tag,"deviceConnect")
            fm.setConnect(flag)
        }

        override fun prepareComplete(item: CWDevice) {
            Log.e(tag,"prepareComplete")
            //todo~~准备完成,此页面未用到
        }

        override fun transferMainElectrodeNotify(value: Int, item: CWDevice) {
            Log.e(tag,"transferMainElectrodeNotify")
            when(value){
                0 -> {
                    fm.showTips("电极未贴合,请贴于患处")
                }
                30 -> {
                    fm.showTips("电极短路,请检查")
                }
                else -> {}
            }
        }

        override fun transferIntensity(value: Int, item: CWDevice) {
            fm.setIntensityNum(value)
        }

        override fun transferMainElectrodeQuery(value: Int, item: CWDevice) {
            Log.e(tag,"transferMainElectrodeQuery")
            when (value){
                0 -> {
                    fm.showTips("电极未贴合,请贴于患处")
                }
                30 -> {
                    fm.showTips("电极短路,请检查")
                }
                else -> {
                    item.startCureAction()
                }
            }
        }

        override fun transferPower(value: Int, item: CWDevice) {
            Log.e(tag,"transferPower->$value")
            fm.setPowerIcon(value)
        }

        override fun prepareFail(error: String, item: CWDevice) {
            Log.e(tag,"prepareFail")
            //todo:准备失败～～此页面未用到
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

}