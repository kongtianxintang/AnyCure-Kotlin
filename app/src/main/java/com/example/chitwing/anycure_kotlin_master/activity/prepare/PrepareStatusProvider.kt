package com.example.chitwing.anycure_kotlin_master.activity.prepare

import android.content.Context
import com.example.chitwing.anycure_kotlin_master.base.CWBaseProvider
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.ble.CWDevice
import com.example.chitwing.anycure_kotlin_master.ble.CWDeviceInterface
import com.example.chitwing.anycure_kotlin_master.unit.CWMessageEvent
import com.example.chitwing.anycure_kotlin_master.unit.showToast



/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/29
 * QQ/Tel/Mail:383118832
 * Description:外设状态控制
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class PrepareStatusProvider(private val context: PrepareActivity) :CWBaseProvider(context){

    var mDevice:CWDevice? = null

    override fun fetchDataSource() {

    }

    /**
     * 开始写入数据
     * */
    fun beginWriteData(){
        if (CWBleManager.mCWDevices.isNotEmpty()){
            val last = CWBleManager.mCWDevices.last()
            last.mCallback = deviceDataCallback
            last.writeData()
            context.startButton.isEnabled = false
            mDevice = last
        }
    }

    /**
     * 电极查询
     * */
    fun electrodeQuery(){
        mDevice?.gattWrite?.cwBleWriteElectrodeQuery()
    }


    private val deviceDataCallback = object :CWDeviceInterface {
        override fun cureEndEvent(item: CWDevice) {

        }

        override fun cureStartEvent(item: CWDevice) {
            enterCurePage()
        }

        override fun cureStopEvent(item: CWDevice) {

        }

        override fun deviceCloseEvent(item: CWDevice) {
            //todo:推出
        }

        override fun deviceConnect(flag: Boolean, item: CWDevice) {

        }

        override fun prepareComplete(item: CWDevice) {
            context.runOnUiThread {
                context.startButton.isEnabled = true
                context.showToast("外设准备就绪")
            }
        }

        override fun prepareFail(error: String, item: CWDevice) {
            context.runOnUiThread {
                context.startButton.isEnabled = false
                context.showToast(error)
            }
        }

        override fun transferIntensity(value: Int, item: CWDevice) {

        }

        override fun transferMainElectrodeNotify(value: Int, item: CWDevice) {

        }

        override fun transferMainElectrodeQuery(value: Int, item: CWDevice) {
            context.runOnUiThread {
                when(value){
                    0 ->{
                        context.showToast("电极贴合异常,请贴合于患处")
                    }
                    30 ->{
                        context.showToast("电极短路,请贴合于患处")
                    }
                    else -> {
                        context.showToast("正常,可以去理疗页面了")
                        mDevice?.gattWrite?.cwBleWriteStartCure()
                    }
                }
            }
        }

        override fun transferPlayDuration(value: Int, item: CWDevice) {

        }

        override fun transferPower(value: Int, item: CWDevice) {

        }

    }


    /**
     * 进入理疗页面
     * */
    private fun enterCurePage(){
        context.setResult(0x01)
        context.finish()
    }

}