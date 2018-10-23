package com.example.chitwing.anycure_kotlin_master.download

import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.chitwing.anycure_kotlin_master.model.Version

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/10/15
 * QQ/Tel/Mail:383118832
 * Description:检查是否有新版本apk
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWCheckVersionProvider {

    fun checkVersion(callback:CheckVersionInterface?,map: Map<String,String>){
        val call = NetRequest.checkVersion(map)
        call.enqueue(object : Callback<Version> {
            override fun onResponse(call: Call<Version>, response: Response<Version>) {
                response.body()?.let {
                    DBHelper.removeAll(Version::class.java)
                    DBHelper.insert(it,Version::class.java)
                    when (it.code){
                        1 -> {
                            callback?.checkVersion(true,it.url,it.content,it.version)
                        }
                        else -> {
                            callback?.checkVersion(false,it.url,it.content,it.version)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Version>, t: Throwable) {
                callback?.checkVersion(false,null,"请求失败",null)
            }
        })
    }

    /**
     * 请求的
     * */
    interface CheckVersionInterface {
        fun checkVersion(flag:Boolean,url:String?,desc:String?,ver: String?)
    }

}