package com.example.chitwing.anycure_kotlin_master.activity.bind

import com.example.chitwing.anycure_kotlin_master.base.CWBaseProvider
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.model.BindDevice

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/21
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWBindProvider(private val context: BindActivity) : CWBaseProvider(context) {

    override fun fetchDataSource() {
        val list = DBHelper.findAll(BindDevice ::class.java)
        list?.let {
            context.mDataSet.clear()
            context.mDataSet.addAll(it)
            context.mAdapter!!.notifyDataSetChanged()
        }
    }
}