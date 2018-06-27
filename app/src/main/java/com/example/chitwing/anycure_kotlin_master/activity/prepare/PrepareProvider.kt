package com.example.chitwing.anycure_kotlin_master.activity.prepare

import android.content.Context
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.base.CWBaseProvider
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.model.PrepareHint
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/27
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class PrepareProvider(private val context: Context) :CWBaseProvider(context) {


    private var mDataSet:List<PrepareHint>? = null

    /**
     * 网络请求数据
     * */
    override fun fetchDataSource() {
        val map = mapOf("channels" to CWBleManager.configure.channel.NUM_CODE)
        val call = NetRequest.prepareContentAction(map)
        call.enqueue( object :Callback<List<PrepareHint>>{

            override fun onFailure(call: Call<List<PrepareHint>>?, t: Throwable?) {
                Log.e(tag,"网络请求错误${t.toString()}")
            }

            override fun onResponse(call: Call<List<PrepareHint>>?, response: Response<List<PrepareHint>>?) {
                response?.body()?.let {
                    saveData(it)
                }
            }
        })
    }

    /**
     * 保存数据到数据库
     * */
    private fun saveData(list:List<PrepareHint>){
        list.forEach {
            Log.e(tag,"title->${it.title} content->${it.content}")
        }
        DBHelper.removeAll(PrepareHint ::class.java)
        DBHelper.insert(list,PrepareHint::class.java)
    }

    /**
     * 获取数据
     * */
    private fun getDataSource(){
        mDataSet = DBHelper.findAll(PrepareHint ::class.java)
    }

    /**
     * 根据数据显示页面
     * */
    fun configureView(){
        getDataSource()
        val ac = context as? PrepareActivity
        ac?.let {
            mDataSet?.let {
                val titleHint = it[0]
                ac.titleTextView.text = titleHint.getSpan()
                if (it.size > 1){
                    for (i in 1 .. (it.size - 1)){
                        val item = it[i]
                        val name = item.title ?: "默认"
                        val tab = ac.tabLayout.newTab()
                        tab.text = name
                        ac.tabLayout.addTab(tab)
                    }
                    ac.content.text = it[1].getReplaceContent()
                }
            }
        }
    }

    /**
     * 切换tab 变化数据
     * */
    fun switchTab(index:Int){
        val i = index + 1
        mDataSet?.let {
            if (it.size > i){
                val ac = context as? PrepareActivity
                ac?.let {
                    val item = mDataSet!![i]
                    ac.content.text = item.getReplaceContent()
                }
            }
        }
    }


}