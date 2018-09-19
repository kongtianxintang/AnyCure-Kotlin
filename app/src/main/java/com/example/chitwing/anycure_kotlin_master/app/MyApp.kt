package com.example.chitwing.anycure_kotlin_master.app

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
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

    companion object {
        const val mTag: String = "AnyCure-Kotlin"
        private var instance:MyApp? = null
        fun getApp() = instance!!
    }

    lateinit var boxStore: BoxStore
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        boxStore = MyObjectBox.builder().androidContext(this).build()
        Log.d(mTag, "启动boxStore")

        ProcessLifecycleOwner.get().lifecycle.addObserver(MyLifecycle())
    }

    inner class MyLifecycle: LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun create() {
            Log.d(mTag,"create")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun start() {
            Log.d(mTag,"start")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun resume() {
            Log.d(mTag,"resume")
            CWBleManager.mCWDevices.forEach { it.queryCommunicationSerialNumber() }
        }


        //此后App进入不可见状态/后台
        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun pause() {
            Log.d(mTag,"pause")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun stop() {
            Log.d(mTag,"stop")
        }
    }

}
