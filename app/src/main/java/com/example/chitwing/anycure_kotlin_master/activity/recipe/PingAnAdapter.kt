package com.example.chitwing.anycure_kotlin_master.activity.recipe

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.base.CWOnItemClickListener
import com.example.chitwing.anycure_kotlin_master.model.Recipe
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import com.example.chitwing.anycure_kotlin_master.unit.loadRadius
import com.example.chitwing.anycure_kotlin_master.unit.loader

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
class PingAnAdapter (private val dataSet:List<Recipe>?, private val context:Activity) : RecyclerView.Adapter<PingAnAdapter.ViewHolder>() {

    private val tag = "RecipeAdapter"

    /**
     * 点击事件
     * */
    var onItemClickListenner:CWOnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.recipe_item, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet!![position]
        holder.mTextView.text = item.recipeName
        holder.mDesc.text = item.recipeUse
        item.recipeIcon?.let {
            val path = NetRequest.IMAGE_BASE_PATH + it
            holder.mImg.loadRadius(context,path,10)
        }

        holder.itemView.setOnClickListener {
            if (onItemClickListenner != null){
                onItemClickListenner!!.onItemClick(holder.itemView,position)
            }
        }
    }


    override fun getItemCount(): Int {
        dataSet?.let {
            return it.count()
        }
        return 0
    }


    class ViewHolder(v: View):RecyclerView.ViewHolder(v) {
        val mTextView:TextView
        val mImg:ImageView
        val mDesc:TextView
        init {
            mTextView = v.findViewById(R.id.recipe_item_title)
            mImg = v.findViewById(R.id.recipe_item_img)
            mDesc = v.findViewById(R.id.recipe_item_desc)
        }
    }

}
