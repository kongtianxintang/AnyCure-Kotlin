package com.example.chitwing.anycure_kotlin_master.download

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadResProvider( private val ac: Context,private val callback:DownloadFileTask.DownloadFileInterface) {


    private var mTag = "下载资源"
    private var mTask: DownloadFileTask? = null
    /**
     * 开始下载
     * */
    fun start(url: String){
        mTask = DownloadFileTask()

        val dir = ac.getExternalFilesDir(null)
        val file = File(dir,DownloadConfigure.resName)
        mTask!!.file = file
        mTask!!.callback = callback
        mTask!!.execute(url)
    }

    /**
     * 取消下载
     * */
    fun cancelTask(){
        mTask?.cancel(true)
    }

}