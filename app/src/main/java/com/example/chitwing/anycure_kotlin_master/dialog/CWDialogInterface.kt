package com.example.chitwing.anycure_kotlin_master.dialog

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/7/31
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
interface CWDialogInterface {
    /**
     * 点击了button    flag->false 为cancel按钮  flag->true 为sure按钮
     * */
    fun onClickButton(flag:Boolean,item:CWDialog)
}