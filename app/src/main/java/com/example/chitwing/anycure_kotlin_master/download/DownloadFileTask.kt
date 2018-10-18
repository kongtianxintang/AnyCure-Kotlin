package com.example.chitwing.anycure_kotlin_master.download

import android.os.AsyncTask
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/10/15
 * QQ/Tel/Mail:383118832
 * Description:文件下载类
 * Others:
 * Modifier:
 * Reason:
 *************************************************************/
class DownloadFileTask: AsyncTask<String,Int,Boolean>(){

    private val mTag = "下载文件类"
    private var mIsSuccessful: Boolean = false
    var file: File? = null
    var callback: DownloadFileInterface? = null

    interface DownloadFileInterface {
        /**
         * 下载成功与否
         * */
        fun downloadSuccessful(flag: Boolean)
        /**
         * 下载进度
         * */
        fun downloadProgress(arg: Int)
    }

    override fun doInBackground(vararg params: String?): Boolean {
        params.firstOrNull()?.let {
            downloadRes(it)
        }
        return mIsSuccessful
    }

    override fun onCancelled() {
        super.onCancelled()
        mIsSuccessful = false
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        Log.d(mTag,"执行成功与否->$result")
        result?.let {
            callback?.downloadSuccessful(it)
        }
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        values.firstOrNull()?.let {
            Log.d(mTag,"下载进度->$it")
            callback?.downloadProgress(it)
        }
    }


    private fun downloadRes(url: String){
        if (file == null){
            mIsSuccessful = false
            assert(true,( {
                Log.e(mTag,"file 不能为空")
            }))
            return
        }
        var connect: HttpURLConnection? = null
        var zipIn: InputStream? = null
        var zipOut: FileOutputStream? = null
        try {
            val request = URL(url)
            connect = request.openConnection() as HttpURLConnection
            connect.connectTimeout = 200 * 1000
            connect.readTimeout = 200 * 1000
            if (connect.responseCode == HttpURLConnection.HTTP_OK){
                if (!file!!.exists()){
                    file!!.createNewFile()
                }
                val buffer = ByteArray(1024 * 4)
                val fileLength = connect.contentLength
                var len = 0
                val off = 0
                var sum: Long = 0
                zipIn =  connect.inputStream
                zipOut =  FileOutputStream(file)
                while (zipIn.read(buffer).apply { len = this } > 0){
                    zipOut.write(buffer,off,len)
                    sum += len.toLong()
                    val progress = sum * 1.0f / fileLength * 100
                    publishProgress(progress.toInt())
                }
                Log.d(mTag,"下载成功-呵呵")
                mIsSuccessful = true
            }
        }catch (e: IOException){
            mIsSuccessful = false
            Log.d(mTag,"下载zip失败 错误:$e")
        }finally {
            connect?.disconnect()
            zipIn?.close()
            zipOut?.close()
        }
    }
}
