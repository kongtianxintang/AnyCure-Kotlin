package com.example.chitwing.anycure_kotlin_master.ble

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/7
 * QQ/Tel/Mail:383118832
 * Description:蓝牙的状态接口
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
/**
 * 蓝牙状态
 * */
interface CWBleStatusInterface {
    fun bleStatus(arg:CWBleStatus)
    /**
     * 创建了CWDevice对象
     * */
    fun onCreateCWDevice(arg:CWDevice)
}


