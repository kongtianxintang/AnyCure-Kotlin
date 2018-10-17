package com.example.chitwing.anycure_kotlin_master.download

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import okhttp3.ResponseBody
import retrofit2.Response
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

class DownloadAPKProvider(private val ac:Context) {

    private val mTag = "下载apk"
    private var mTask: DownloadTask? = null
    private var mCallback: DownloadApkInterface? = null

    fun setCallback(arg: DownloadApkInterface?){
        mCallback = null
        mCallback = arg
    }


    fun downloadTask(url:String){
        mTask = DownloadTask()
        mTask!!.execute(url)
    }


    inner class DownloadTask:AsyncTask<String,Int,Boolean>(){

        private var isSuccessful: Boolean = false

        override fun doInBackground(vararg params: String?): Boolean {
            val url = params.firstOrNull()
            url?.let {
               downloadApk(it)
            }

            return isSuccessful
        }

        override fun onCancelled(result: Boolean?) {
            isSuccessful = false
        }

        override fun onPostExecute(result: Boolean?) {
            result?.let {
                mCallback?.downloadSuccess(it)
            }
        }

        override fun onProgressUpdate(vararg values: Int?) {
            val p = values.firstOrNull()
            p?.let {
                mCallback?.downloadProgress(it)
            }
        }

        private fun downloadApk(path: String){
            var input: InputStream? = null
            var output: OutputStream? = null
            var connection: HttpURLConnection? = null
            try {
                val url = URL(path)
                connection = url.openConnection() as HttpURLConnection
                if (connection.responseCode != HttpURLConnection.HTTP_OK){
                    isSuccessful = false
                }
                val fileLength = connection.contentLength
                input = connection.inputStream
                val dir = ac.getExternalFilesDir(null)
                val file = File(dir,"may_able.apk")
                if (!file.exists()){
                    file.createNewFile()
                }
                output = FileOutputStream(file)
                val buffer = ByteArray(1024 * 4)
                var len = 0
                val off = 0
                var sum: Long = 0
                while (input!!.read(buffer).apply { len = this } > 0){
                    output.write(buffer,off,len)
                    sum += len.toLong()
                    val progress = sum * 1.0f / fileLength * 100
                    publishProgress(progress.toInt())
                }
                isSuccessful = true

            }catch (e: IOException){
                isSuccessful = false
            }
            finally {
                input?.close()
                output?.close()
                connection?.disconnect()
            }
        }
    }



    interface DownloadApkInterface {
        fun downloadProgress(arg: Int)
        fun downloadSuccess(flag: Boolean)
    }
}