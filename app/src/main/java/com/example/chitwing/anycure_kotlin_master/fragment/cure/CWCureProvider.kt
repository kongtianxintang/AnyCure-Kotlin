package com.example.chitwing.anycure_kotlin_master.fragment.cure

import com.example.chitwing.anycure_kotlin_master.ble.CWDevice
import com.example.chitwing.anycure_kotlin_master.ble.CWDeviceInterface
import com.example.chitwing.anycure_kotlin_master.ble.CWDeviceStatusInterface
import com.orhanobut.logger.Logger

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
    
    /**
     * 接口
     * */
    val callback = object :CWDeviceInterface {
        override fun cureEndEvent(index:Int,item: CWDevice) {
            Logger.d("cureEndEvent")
            fm.endStatus()
        }

        override fun cureStartEvent(item: CWDevice) {
            Logger.d("cureStartEvent")
            fm.startStatus()
        }

        override fun cureStopEvent(item: CWDevice) {
            Logger.d("cureStopEvent")
            fm.stopStatus()
        }

        override fun transferPlayDuration(value: Int, item: CWDevice) {
            fm.setLeftTime(value)
        }

        override fun deviceCloseEvent(index:Int,item: CWDevice) {
            Logger.d("deviceCloseEvent")
            fm.endStatus()
        }

        override fun deviceConnect(flag: Boolean, item: CWDevice) {
            Logger.d("deviceConnect")
            fm.setLinkStatus(flag)
        }

        override fun prepareComplete(item: CWDevice) {
            Logger.d("prepareComplete")
            //todo~~准备完成,此页面未用到
        }

        override fun transferMainElectrodeNotify(value: Int, item: CWDevice) {
            Logger.d("transferMainElectrodeNotify")
            //todo:电极贴贴合状态～～硬件通知
        }

        override fun transferIntensity(value: Int, item: CWDevice) {
            fm.setIntensity(value)
        }

        override fun transferMainElectrodeQuery(value: Int, item: CWDevice) {
            Logger.d("transferMainElectrodeQuery")
            //todo:电极贴贴合状态～～查询
        }

        override fun transferPower(value: Int, item: CWDevice) {
            Logger.d("transferPower->$value")
            fm.setPower(value)
        }

        override fun prepareFail(error: String, item: CWDevice) {
            Logger.d("prepareFail")
            //todo:准备失败～～此页面未用到
        }

    }

    val statusCallback = object :CWDeviceStatusInterface {

        override fun transferDeviceClose(index:Int,item: CWDevice) {
            fm.minusDevice()
        }

        override fun transferDevicePlayComplete(index:Int,item: CWDevice) {
            fm.minusDevice()
        }
    }



}