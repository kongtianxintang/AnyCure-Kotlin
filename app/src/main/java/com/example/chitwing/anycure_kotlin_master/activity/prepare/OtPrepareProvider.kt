package com.example.chitwing.anycure_kotlin_master.activity.prepare

import com.example.chitwing.anycure_kotlin_master.base.CWBaseProvider
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.ble.CWDevice
import com.example.chitwing.anycure_kotlin_master.ble.CWDeviceInterface
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.model.PrepareHint
import com.example.chitwing.anycure_kotlin_master.unit.showToast
import kotlinx.android.synthetic.main.activity_ot_prepare.*

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/8/27
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class OtPrepareProvider (private val context:OtPrepareActivity): CWBaseProvider(context){

    private var mDataSet:List<PrepareHint>? = null

    override fun fetchDataSource() {
            getDataSource()
            switchIndex(0)
    }


    /**
     * 获取数据
     * */
    private fun getDataSource(){
        mDataSet = DBHelper.findAll(PrepareHint ::class.java)
    }

    fun switchIndex(arg:Int){
        val index = arg + 1
        mDataSet?.let {
            if (it.count() > index) {
                val item = it[index]
                context.midTextView.text = item.getReplaceContent()
            }
        }
    }
    /**
     * 关机
     * */
    private fun deviceClose(){
        context.runOnUiThread {
            context.onBackPressed()
        }
    }

    /**
     * 进入理疗页面
     * */
    private fun enterCurePage(){
        context.setResult(0x01)
        context.finish()
    }

    /**
     * 外设回调接口
     * */
    val deviceInterface = object :CWDeviceInterface {

        override fun cureEndEvent(item: CWDevice) {
            deviceClose()
        }

        override fun cureStartEvent(item: CWDevice) {
            item.mCallback = null
            enterCurePage()
        }

        override fun cureStopEvent(item: CWDevice) {

        }

        override fun deviceCloseEvent(item: CWDevice) {
            deviceClose()
        }

        override fun deviceConnect(flag: Boolean, item: CWDevice) {
            context.runOnUiThread {
                context.prepareButton.isEnabled = flag
                val desc = if (flag) "设备连接成功" else "设备断开连接"
                context.showToast(desc)
            }
        }

        override fun prepareComplete(item: CWDevice) {
            context.runOnUiThread {
                context.prepareButton.isEnabled = true
            }
        }

        override fun prepareFail(error: String, item: CWDevice) {
            context.runOnUiThread {
                context.showToast(error)
            }
        }

        override fun transferIntensity(value: Int, item: CWDevice) {

        }

        override fun transferMainElectrodeNotify(value: Int, item: CWDevice) {
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
                    }
                }
            }
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
                        val last = CWBleManager.mCWDevices.lastOrNull()
                        last?.gattWrite?.cwBleWriteStartCure()
                    }
                }
            }
        }

        override fun transferPlayDuration(value: Int, item: CWDevice) {

        }

        override fun transferPower(value: Int, item: CWDevice) {

        }
    }
}