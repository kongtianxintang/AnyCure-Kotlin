package com.example.chitwing.anycure_kotlin_master.app

import android.app.Application
import android.content.Context
import android.nfc.Tag
import android.os.AsyncTask
import android.util.Log
import com.bumptech.glide.annotation.GlideModule
import com.example.chitwing.anycure_kotlin_master.model.MyObjectBox
import io.objectbox.BoxStore
import kotlinx.coroutines.experimental.launch

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

    companion object {
        val Tag: String = "AnyCure-Kotlin"
        private var instance:MyApp? = null
        fun getApp() = instance!!
    }

    lateinit var boxStore: BoxStore
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        boxStore = MyObjectBox.builder().androidContext(this).build()
        Log.d(Tag, "启动boxStore")
    }


}