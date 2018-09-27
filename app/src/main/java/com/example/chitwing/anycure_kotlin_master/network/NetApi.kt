package com.example.chitwing.anycure_kotlin_master.network

import com.example.chitwing.anycure_kotlin_master.model.*
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/5
 * QQ/Tel/Mail:383118832
 * Description:api
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
interface NetApi {
    /**
     * 获取处方
     * */
    @POST("/recipe/getrecipeinfo")
    @FormUrlEncoded
    fun fetchRecipeList(@FieldMap body:Map<String,String>): Call<List<Recipe>>

    /**
     * 上传日志:其实是记录用户使用的处方
     * */
    @POST("/Log")
    @FormUrlEncoded
    fun userRecipeLog(@FieldMap body: Map<String, String>) : Call<Login>

    /**
     * 登录
     * */
    @POST("/Login/login")
    @FormUrlEncoded
    fun loginAction(@FieldMap body: Map<String, String>) : Call<Login>

    /**
     * 准备页 提示内容
     * */
    @POST("/recipe/promptInfo")
    @FormUrlEncoded
    fun prepareContent(@FieldMap body: Map<String, String>) :Call<List<PrepareHint>>

    /**
     * 获取验证码
     * */
    @POST("sms/reqCode")
    @FormUrlEncoded
    fun fetchSMSCode(@FieldMap body: Map<String, String>) :Call<SMSCode>

    /**
     * 注册
     * */
    @POST("/User/userReg")
    @FormUrlEncoded
    fun register(@FieldMap body: Map<String, String>) :Call<CWRegister>

    /**
     * 版本更新
     * */
    @POST("checkVersion")
    @FormUrlEncoded
    fun checkVersion(@FieldMap body: Map<String, String>): Call<Version>


}