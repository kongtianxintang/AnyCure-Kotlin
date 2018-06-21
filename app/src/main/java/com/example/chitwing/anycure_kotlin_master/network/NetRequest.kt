package com.example.chitwing.anycure_kotlin_master.network

import com.example.chitwing.anycure_kotlin_master.model.Login
import com.example.chitwing.anycure_kotlin_master.model.Recipe
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

    fun loginAction(map: Map<String,String>) : Call<Login> {
        val body = createPair(map)
        return api.loginAction(body)
    }

    fun fetchRecipeAction(map: Map<String, String>) : Call<List<Recipe>> {
        val body = createPair(map)
        return api.fetchRecipeList(body)
    }

    private fun createPair(map: Map<String, String>) :Map<String,String>{
        val json = gson.toJson(map)
        val body = mapOf("data" to json)
        return body
    }
}