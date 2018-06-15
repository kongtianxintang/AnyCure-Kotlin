package com.example.chitwing.anycure_kotlin_master.base

import android.content.Context

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/14
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
abstract class CWBaseProvider(private val context: Context) {
    /**
     * 获取数据
     * **/
    abstract fun fetchDataSource()
}