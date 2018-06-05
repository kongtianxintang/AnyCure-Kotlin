package com.example.chitwing.anycure_kotlin_master.app

import android.app.Application
import com.example.chitwing.anycure_kotlin_master.model.MyObjectBox
import io.objectbox.BoxStore

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/5
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class MyApp :Application() {

    lateinit var boxStore: BoxStore
        private  set

    override fun onCreate() {
        super.onCreate()
        boxStore = MyObjectBox.builder().androidContext(this).build()
    }
}