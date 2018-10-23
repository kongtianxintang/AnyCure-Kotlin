package com.example.chitwing.anycure_kotlin_master.ot

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.model.Recipe
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import com.example.chitwing.anycure_kotlin_master.unit.loader

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/8/22
 * QQ/Tel/Mail:383118832
 * Description:处方库组(保健方案库)
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class RecipeNormalAdapter(val context:Context) :RecyclerView.Adapter<RecipeNormalAdapter.NormalItemView> (){

    var callback:RecipeInterface? = null
    private var mDataSource:List<Recipe>? = null
    fun setDataSource(list:List<Recipe>?){
        mDataSource = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return  if (mDataSource == null) 0 else mDataSource!!.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeNormalAdapter.NormalItemView {
        val v = LayoutInflater.from(parent.context!!).inflate(R.layout.ot_normal_item_view,parent,false)
        return RecipeNormalAdapter.NormalItemView(v)
    }

    override fun onBindViewHolder(holder: RecipeNormalAdapter.NormalItemView, position: Int) {
        val obj = mDataSource!![position]
        holder.textView.text = obj.recipeName
        holder.imageView.loader(context,obj.recipeIcon)
        holder.itemView.setOnClickListener {
            if (callback != null){
                callback!!.didSelectItem(obj)
            }
        }
    }

    class NormalItemView(v:View) :RecyclerView.ViewHolder(v){
        val textView:TextView = v.findViewById(R.id.ot_normal_item_text_view)
        val imageView:ImageView = v.findViewById(R.id.ot_normal_item_image_view)
    }
}