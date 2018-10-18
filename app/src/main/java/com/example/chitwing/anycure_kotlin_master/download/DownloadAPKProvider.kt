package com.example.chitwing.anycure_kotlin_master.download

import android.content.Context
import android.os.AsyncTask
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/10/15
 * QQ/Tel/Mail:383118832
 * Description:检查是否有新版本apk 并且下载
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/

class DownloadAPKProvider(private val ac:Context,private val callback:DownloadFileTask.DownloadFileInterface) {

    private var mTask: DownloadFileTask? = null

    fun downloadTask(url:String){
        mTask = DownloadFileTask()
        val dir = ac.getExternalFilesDir(null)
        val file = File(dir,DownloadConfigure.apkName)
        mTask!!.file = file
        mTask!!.callback = callback
        mTask!!.execute(url)
    }

    fun cancelTask(){
        mTask?.cancel(true)
    }

}