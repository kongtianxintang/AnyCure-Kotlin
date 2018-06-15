package com.example.chitwing.anycure_kotlin_master.activity.recipe

import android.content.Context
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.base.CWBaseProvider
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.model.Recipe
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import com.example.chitwing.anycure_kotlin_master.unit.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

}