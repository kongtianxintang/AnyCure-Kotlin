package com.example.chitwing.anycure_kotlin_master.dialog

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
interface BleDialogInterface {
    /**
     * 表明已经链接到了一个设备
     * */
    fun connectDevice()
    /**
     * 点击了button cancel or sure
     * */
    fun onClickButton(flag:Boolean)
}