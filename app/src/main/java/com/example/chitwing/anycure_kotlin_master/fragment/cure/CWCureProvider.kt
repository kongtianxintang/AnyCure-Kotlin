package com.example.chitwing.anycure_kotlin_master.fragment.cure

import android.util.Log
import com.example.chitwing.anycure_kotlin_master.ble.CWDevice
import com.example.chitwing.anycure_kotlin_master.ble.CWDeviceInterface

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/7/12
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWCureProvider(private val fm:CureFragment) {


    private val tag = "CWCureProvider"

    /**
     * 接口
     * */
    val callback = object :CWDeviceInterface {
        override fun cureEndEvent(item: CWDevice) {
            Log.e(tag,"cureEndEvent")
            fm.endStatus()
        }

        override fun cureStartEvent(item: CWDevice) {
            Log.e(tag,"cureStartEvent")
            fm.startStatus()
        }

        override fun cureStopEvent(item: CWDevice) {
            Log.e(tag,"cureStopEvent")
            fm.stopStatus()
        }

        override fun transferPlayDuration(value: Int, item: CWDevice) {
            Log.e(tag,"transferPlayDuration")
            fm.setPlayTime(value)

        }

        override fun deviceCloseEvent(item: CWDevice) {
            Log.e(tag,"deviceCloseEvent")
            fm.endStatus()
        }

        override fun deviceConnect(flag: Boolean, item: CWDevice) {
            Log.e(tag,"deviceConnect")
            //todo~~~~~设备连接情况
        }

        override fun prepareComplete(item: CWDevice) {
            Log.e(tag,"prepareComplete")
            //todo~~准备完成,此页面未用到
        }

        override fun transferMainElectrodeNotify(value: Int, item: CWDevice) {
            Log.e(tag,"transferMainElectrodeNotify")
            //todo:电极贴贴合状态～～硬件通知
        }

        override fun transferIntensity(value: Int, item: CWDevice) {
            fm.setIntensity(value)
        }

        override fun transferMainElectrodeQuery(value: Int, item: CWDevice) {
            Log.e(tag,"transferMainElectrodeQuery")
            //todo:电极贴贴合状态～～查询
        }

        override fun transferPower(value: Int, item: CWDevice) {
            Log.e(tag,"transferPower->$value")
            fm.setPower(value)
        }

        override fun prepareFail(error: String, item: CWDevice) {
            Log.e(tag,"prepareFail")
            //todo:准备失败～～此页面未用到
        }

    }


}