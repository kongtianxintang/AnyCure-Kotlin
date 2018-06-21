package com.example.chitwing.anycure_kotlin_master.activity.bind

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.base.CWOnItemClickListener
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
class CWBindAdapter(private val dataSet:List<BindDevice>?, private val context: Activity) :RecyclerView.Adapter<CWBindAdapter.ViewHolder>() {


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
            //todo:点击解绑 删除
        }
    }

    override fun getItemCount(): Int {
        dataSet?.let {
            return it.size
        }
        return  0
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val mMac: TextView
        val mName: TextView
        val mButton: Button

        init {
            mName = v.findViewById(R.id.bind_item_device_name)
            mMac = v.findViewById(R.id.bind_item_device_mac)
            mButton = v.findViewById(R.id.bind_item_device_button)
        }

    }
}