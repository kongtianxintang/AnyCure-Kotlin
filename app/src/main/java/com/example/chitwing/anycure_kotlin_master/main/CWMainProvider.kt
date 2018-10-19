package com.example.chitwing.anycure_kotlin_master.main

import android.util.Log
import com.example.chitwing.anycure_kotlin_master.MainActivity
import com.example.chitwing.anycure_kotlin_master.base.CWBaseProvider
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.dialog.CWDownloadResDialog
import com.example.chitwing.anycure_kotlin_master.download.DownloadConfigure
import com.example.chitwing.anycure_kotlin_master.download.DownloadFileTask
import com.example.chitwing.anycure_kotlin_master.download.DownloadResProvider
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import com.example.chitwing.anycure_kotlin_master.unit.SharedPreferencesHelper
import com.example.chitwing.anycure_kotlin_master.unit.Unit
import com.orhanobut.logger.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/10/19
 * QQ/Tel/Mail:383118832
 * Description:首页数据提供类 主要作用下载文件资源
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/

class CWMainProvider(private val  ac:MainActivity): CWBaseProvider(ac){


    /**图片资源包下载是否成功*/
    private var mImageSuccess = false
    /**处方库资源包下载是否成功  算了不在此次请求*/
    private var mRecipeSuccess = true
    /** 提示框 **/
    private var mDialog: CWDownloadResDialog? = null

    override fun fetchDataSource() {
        val update = SharedPreferencesHelper.getObject(SharedPreferencesHelper.timeKey,1.toLong()) as Long
        Logger.d("更新时间->$update")
        if (update == 1.toLong()){
            downloadRes()
            return
        }
        val body = mapOf("channels" to CWBleManager.configure.channel.NUM_CODE)
        val call = NetRequest.dataRefresh(body)
        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Logger.d("请求回调 错误->$t")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Logger.d("请求回调 时间戳->${response.body()}")
                response.body()?.let {
                    val time = it.toLong() * 1000
                    val date = Date(time)
                    val old = Date(update)
                    Logger.d("时间戳->$time 时间->$date")
                    if (date > old) {
                        downloadRes()
                    }
                }
            }
        })
    }

    /**
     * 下载 处方库 及 图片资源
     * 都下载完成后加载view 否则不让使用
     * 分两部分
     * todo:后续优化代码 使用协程
     * */
    private fun downloadRes(){
        Logger.d("去更新资源")
        //1.下载处方库
        downloadRecipeRec()
        //2.下载图片资源
        downloadImageRes()
    }

    private fun downloadImageRes(){
        downloadResZip()
        if (mDialog == null){
            mDialog = CWDownloadResDialog()
            mDialog!!.show(ac.fragmentManager,"zip")
            mDialog!!.callback = object : CWDownloadResDialog.ResDialogInterface {
                override fun refreshAction() {
                    reFetchResPackage()
                }
            }
        }
    }

    private fun downloadRecipeRec(){

    }

    /**
     * 重新获取资源包
     * */
    fun reFetchResPackage(){
        if (mImageSuccess && mRecipeSuccess){
            //都成功
        }else if (mImageSuccess && !mRecipeSuccess){
            //处方不成功
            downloadRecipeRec()
        }else if (!mImageSuccess && mRecipeSuccess){
            //资源包不成功
            downloadImageRes()
        }else {
            //都不成功
            downloadRes()
        }
    }

    /**
     * 下载资源
     * */
    private fun downloadResZip(){
        val provider = DownloadResProvider(ac,callback = downloadResCallback)
        provider.resPackage()
    }

    private val downloadResCallback = object : DownloadFileTask.DownloadFileInterface {

        override fun downloadProgress(arg: Int) {
//            Logger.d("进度->$arg")
            mDialog?.setProgressBarValue(arg)
        }

        override fun downloadSuccessful(flag: Boolean) {
            Logger.d("下载结果->$flag")
            if (flag){
                val dir = ac.getExternalFilesDir(null)
                val zip = File(dir, DownloadConfigure.resName)
                val unzip = File(dir, DownloadConfigure.resTargetName)
                if (!unzip.exists()){
                    unzip.mkdir()
                }
                Unit.unZip(zip,unzip.path)
                /**保存好数据刷新时间**/
                SharedPreferencesHelper.put(SharedPreferencesHelper.timeKey,Date().time)
                //:成功
                ac.downloadResSuccessful()
                mImageSuccess = true
                mDialog?.dismiss()
            }else{
                mDialog?.failInfo("下载失败,请重试")
            }
        }
    }
}