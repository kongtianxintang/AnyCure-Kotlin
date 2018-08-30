package com.example.chitwing.anycure_kotlin_master.network

import android.util.Log
import com.example.chitwing.anycure_kotlin_master.model.Login
import com.example.chitwing.anycure_kotlin_master.model.PrepareHint
import com.example.chitwing.anycure_kotlin_master.model.Recipe
import com.example.chitwing.anycure_kotlin_master.model.SMSCode
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/5
 * QQ/Tel/Mail:383118832
 * Description:网路请求
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
object NetRequest {


    private val mTag = "网络请求"
    /**
     * 主机地址
     * */
    private val CW_HOST_IP = "http://ancure.mayable.com.cn/"

    /**
     * 图片url前缀
     * */
    val IMAGE_BASE_PATH = CW_HOST_IP + "uploadfile/"

    private val gson = Gson()
    private val client = OkHttpClient.Builder().readTimeout(15, TimeUnit.SECONDS).connectTimeout(15, TimeUnit.SECONDS).build();
    private val retro = Retrofit.Builder()
            .baseUrl(CW_HOST_IP)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    private val api = retro.create(NetApi ::class.java)

    /**
     * 登录
     * */
    fun loginAction(map: Map<String,String>) : Call<Login> {
        val body = createPair(map)
        return api.loginAction(body)
    }

    /**
     * 获取处方
     * */
    fun fetchRecipeAction(map: Map<String, String>) : Call<List<Recipe>> {
        val body = createPair(map)
        return api.fetchRecipeList(body)
    }

    /**
     * 获取准备页的提示内容
     * */
    fun prepareContentAction(map: Map<String, String>) :Call<List<PrepareHint>>{
        val body = createPair(map)
        return api.prepareContent(body)
    }

    /**
     * 获取验证码
     * */
    fun fetchSMSCode(map: Map<String, String>) :Call<SMSCode>{
        val body = createPair(map)
        return api.fetchSMSCode(body)
    }

    /**
     * 配置参数
     * */
    private fun createPair(map: Map<String, String>) :Map<String,String>{
        val json = gson.toJson(map)
        Log.d(mTag,"请求参数->$json")
        return mapOf("data" to json)
    }


}