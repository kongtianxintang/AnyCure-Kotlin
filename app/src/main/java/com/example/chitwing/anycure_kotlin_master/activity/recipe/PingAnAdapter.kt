package com.example.chitwing.anycure_kotlin_master.activity.recipe

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/13
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class PingAnAdapter (private val dataSet:List<String>) : RecyclerView.Adapter<PingAnAdapter.ViewHolder>() {

    private val tag = "RecipeAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.recipe_item, parent, false)
        Log.d(tag,"创建视图～～")
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.mTextView?.text = item
    }

    override fun getItemCount() = dataSet.count()

    class ViewHolder(v: View):RecyclerView.ViewHolder(v) {
        var mTextView: TextView? = null
        init {
            mTextView = v.findViewById(R.id.textView)
        }
    }

}
