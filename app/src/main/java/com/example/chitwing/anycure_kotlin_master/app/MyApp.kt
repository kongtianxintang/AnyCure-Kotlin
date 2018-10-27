package com.example.chitwing.anycure_kotlin_master.app

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.BuildConfig
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.model.MyObjectBox
import com.example.chitwing.anycure_kotlin_master.unit.Unit
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
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
        private var instance:MyApp? = null
        fun getApp() = instance!!
    }

    lateinit var boxStore: BoxStore
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        boxStore = MyObjectBox.builder().androidContext(this).build()

        ProcessLifecycleOwner.get().lifecycle.addObserver(MyLifecycle())
        initLogger()

    }

    inner class MyLifecycle: LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun create() {
            Logger.d(" create")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun start() {
            Logger.d("start")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun resume() {
            Logger.d("resume")
            CWBleManager.mCWDevices.forEach { it.queryCommunicationSerialNumber() }
        }


        //此后App进入不可见状态/后台
        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun pause() {
            Logger.d("pause")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun stop() {
            Logger.d("stop")
        }
    }

    /**
     * 注册logger
     * */
    private fun initLogger(){
        Logger.addLogAdapter(object : AndroidLogAdapter(){
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
//                return false
            }

        })
    }

}
