package com.example.chitwing.anycure_kotlin_master.download

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.model.CWResPack
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import com.example.chitwing.anycure_kotlin_master.unit.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
     * 获取资源路径
     * */
    fun resPackage(){
        val map = mapOf("channels" to CWBleManager.configure.channel.NUM_CODE)
        val call = NetRequest.resPackagePath(map)
        call.enqueue(object : Callback<CWResPack> {
            override fun onResponse(call: Call<CWResPack>, response: Response<CWResPack>) {
                val obj = response.body()
                obj?.let {
                    when (it.code) {
                        0 -> {
                            start(it.resurl!!)
                            Log.d(mTag,"资源包地址->${it.resurl}")
                        }
                        else -> {
                            ac.showToast(it.message ?: "已经是最新的资源")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CWResPack>, t: Throwable) {
                ac.showToast("资源下载失败")
            }
        })
    }

    /**
     * 开始下载
     * */
    private fun start(url: String){
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