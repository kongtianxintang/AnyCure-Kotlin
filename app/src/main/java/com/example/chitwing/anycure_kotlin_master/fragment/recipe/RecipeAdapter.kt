package com.example.chitwing.anycure_kotlin_master.fragment.recipe

import android.content.Context
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
class RecipeAdapter(private val dataSet:List<Recipe>,val context: Context) :RecyclerView.Adapter<RecipeAdapter.ViewHolder>(){


    private val tag = "RecipeAdapter"

    /**
     * 点击事件
     * */
    var onItemClickListener: CWOnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.recipe_item, parent, false)
        return RecipeAdapter.ViewHolder(v)
    }


    override fun onBindViewHolder(holder: RecipeAdapter.ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.mTextView.text = item.recipeName
        holder.mDesc.text = item.recipeUse
        item.recipeIcon?.let {
            val path = NetRequest.IMAGE_BASE_PATH + it
            holder.mImg.loadRadius(context,path,10)
        }

        holder.itemView.setOnClickListener {
            if (onItemClickListener != null){
                onItemClickListener!!.onItemClick(holder.itemView,position)
            }
        }
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }


    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val mTextView: TextView = v.findViewById(R.id.recipe_item_title)
        val mImg: ImageView = v.findViewById(R.id.recipe_item_img)
        val mDesc: TextView = v.findViewById(R.id.recipe_item_desc)
    }
}