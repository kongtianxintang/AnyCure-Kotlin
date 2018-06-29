package com.example.chitwing.anycure_kotlin_master.ble

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/13
 * QQ/Tel/Mail:383118832
 * Description:传输设备的各种状态,及数值
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
interface CWDeviceInterface {
    /**
     * 装备完成,可以输出了
     * */
    fun prepareComplete(item:CWDevice)

    /**
     * 状态失败
     * error 失败信息
     * */
    fun prepareFail(error:String,item: CWDevice)

    /**
     * 暂停
     * */
    fun cureStopEvent(item: CWDevice)

    /**
     * 开始
     * */
    fun cureStartEvent(item: CWDevice)

    /**
     * 结束
     * */
    fun cureEndEvent(item: CWDevice)

    /**
     * 设备关机
     * */
    fun deviceCloseEvent(item: CWDevice)

    /**
     * 传输强度
     * */
    fun transferIntensity(value:Int,item: CWDevice)

    /**
     * 传输电量
     * */
    fun transferPower(value: Int,item: CWDevice)

    /**
     * 播放时间
     * */
    fun transferPlayDuration(value: Int,item: CWDevice)

    /**
     * 电极状态 设备主动通知
     * */
    fun transferMainElectrodeNotify(value: Int,item: CWDevice)

    /**
     * 设备连接状态
     * */
    fun deviceConnect(flag:Boolean,item: CWDevice)

    /**
     * 主动获取电极贴状态
     * */
    fun transferMainElectrodeQuery(value: Int,item: CWDevice)
}