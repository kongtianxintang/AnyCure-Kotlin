package com.example.chitwing.anycure_kotlin_master.fragment.recipe

import android.content.Context
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.base.CWBaseProvider
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.model.Recipe
import com.example.chitwing.anycure_kotlin_master.model.RecipeSection
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
 * Date:  2018/6/26
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class RecipeProvider(private val context: Context,private val fm:RecipeFragment):CWBaseProvider(context) {


    override fun fetchDataSource() {
        if(!fm.refreshView!!.isRefreshing){
            fm.refreshView!!.isRefreshing = true
        }

        val map = mapOf("channels" to "00000006")
        val api = NetRequest.fetchRecipeAction(map)
        api.enqueue(object : Callback<List<Recipe>> {

            override fun onResponse(call: Call<List<Recipe>>?, response: Response<List<Recipe>>?) {
                response?.body()?.let {
                    fm.mDataSet.clear()
                    val data = listOf( RecipeSection(0x00,it.filter { it.partId == 0 }),
                            RecipeSection(true,"精品",false),
                            RecipeSection(0x02,it.filter { it.partId == 0 }),
                            RecipeSection(true,"处方库",false),
                            RecipeSection(0x03,it.filter { it.partId == 1 }))
                    fm.mDataSet.addAll(data)
                    saveRecipe(it)
                    fm.mAdapter!!.notifyDataSetChanged()
                    if(fm.refreshView!!.isRefreshing){
                        fm.refreshView!!.isRefreshing = false
                    }
                    context.showToast("数据请求成功")
                }
            }

            override fun onFailure(call: Call<List<Recipe>>?, t: Throwable?) {
                Log.d("RecipeProvider","数据请求失败~${t.toString()}")
                if(fm.refreshView!!.isRefreshing){
                    fm.refreshView!!.isRefreshing = false
                }
                context.showToast("数据请求失败,${t.toString()}")
            }
        })
    }

    /**
     * 保存处方
     * */
    private fun saveRecipe(list: List<Recipe>){
        DBHelper.removeAll(Recipe ::class.java)
        DBHelper.insert(list,Recipe::class.java)
    }
}