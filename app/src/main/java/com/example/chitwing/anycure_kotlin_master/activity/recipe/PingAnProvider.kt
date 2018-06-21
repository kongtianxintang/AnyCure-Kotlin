package com.example.chitwing.anycure_kotlin_master.activity.recipe

import android.content.Context
import android.os.Looper
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.base.CWBaseProvider
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.model.Recipe
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import com.example.chitwing.anycure_kotlin_master.unit.showToast
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Delayed
import kotlin.coroutines.experimental.buildSequence

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/14
 * QQ/Tel/Mail:383118832
 * Description:数据提供 逻辑处理
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class PingAnProvider(private val context: PingAnActivity) : CWBaseProvider(context) {

    private val tag = "PingAnProvider"

    override fun fetchDataSource() {

        testT()

        if(!context.mRefresh.isRefreshing){
            context.mRefresh.isRefreshing = true
        }

        val map = mapOf("channels" to CWBleManager.configure.channel.NUM_CODE)
        val api = NetRequest.fetchRecipeAction(map)
        api.enqueue(object :Callback<List<Recipe>>{

            override fun onResponse(call: Call<List<Recipe>>?, response: Response<List<Recipe>>?) {
                response?.body()?.let {
                    context.mDataSet.clear()
                    context.mDataSet.addAll(it)
                    saveRecipe(it)
                    context.mAdapter!!.notifyDataSetChanged()
                    if(context.mRefresh.isRefreshing){
                        context.mRefresh.isRefreshing = false
                    }
                    context.showToast("数据请求成功")
                }
            }

            override fun onFailure(call: Call<List<Recipe>>?, t: Throwable?) {
                Log.d(tag,"数据请求失败~${t.toString()}")
                if(context.mRefresh.isRefreshing){
                    context.mRefresh.isRefreshing = false
                }
                context.showToast("数据请求失败,${t.toString()}")
            }
        })
    }

    /**
     * 保存处方
     * */
    private fun saveRecipe(list: List<Recipe>){
        DBHelper.removeAll(context,Recipe ::class.java)
        DBHelper.insert(context,list,Recipe::class.java)
    }

    /**
     * 测试协程
     * */
    private fun testT(){
       val job = launch(CommonPool){
           for (i in 0 .. 20){
               Log.e(tag,"我也不知道啥～$i")
           }
           Log.e(tag,"当前线程${Thread.currentThread()}")
        }
        job.start()
        val job2 = launch(CommonPool) {
            for (i in 0 .. 5){
                Log.e(tag,"我也不知道啥222～$i")
            }
            Log.e(tag,"当前线程${Thread.currentThread()}")
        }
        job2.start()

        val job3 = launch(CommonPool) {
            for (i in 0 .. 10){
                Log.e(tag,"我也不知道啥33～$i")
            }
            Log.e(tag,"当前线程${Thread.currentThread()}")
            Log.e(tag,"主线程${Looper.getMainLooper().thread}")
        }
        job3.start()


    }




}