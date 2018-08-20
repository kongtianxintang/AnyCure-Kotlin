package com.example.chitwing.anycure_kotlin_master.activity.bind

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
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
class CWBindAdapter(private val dataSet:List<BindDevice>?, private val context: BindActivity) :RecyclerView.Adapter<CWBindAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CWBindAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.bind_item, parent, false)
        return CWBindAdapter.ViewHolder(v)
    }


    override fun onBindViewHolder(holder: CWBindAdapter.ViewHolder, position: Int) {
        val item = dataSet!![position]
        holder.mMac.text = item.mac
        val indexStr = "$position"
        holder.mName.text = "控制器$indexStr"
        holder.mButton.setOnClickListener {
           /**
            * 删除数据库
            * 删除源数据
            * */
            val temp = context.mDataSet[position]
            DBHelper.remove(temp,BindDevice::class.java)
            context.mDataSet.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        dataSet?.let {
            return it.size
        }
        return  0
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val mMac: TextView = v.findViewById(R.id.bind_item_device_mac)
        val mName: TextView = v.findViewById(R.id.bind_item_device_name)
        val mButton: Button = v.findViewById(R.id.bind_item_device_button)
    }
}