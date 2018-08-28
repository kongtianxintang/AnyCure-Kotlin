package com.example.chitwing.anycure_kotlin_master.activity.prepare

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.model.RecipeUsage
import com.example.chitwing.anycure_kotlin_master.unit.loader

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/8/28
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class OtPrepareAdapter(private val context:Context,private val data:Array<RecipeUsage>) :RecyclerView.Adapter<OtPrepareAdapter.ContentView>(){

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentView {
        val v = LayoutInflater.from(parent.context!!).inflate( R.layout.ot_prepare_item_view,parent,false)
        return ContentView(v)
    }

    override fun onBindViewHolder(holder: ContentView, position: Int) {
        val obj = data[position]
        holder.img.loader(context,obj.img)
    }

    class ContentView(v:View) :RecyclerView.ViewHolder(v){
        val img:ImageView = v.findViewById(R.id.ot_prepare_item_image_view)
    }
}