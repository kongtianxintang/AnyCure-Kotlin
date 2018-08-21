package com.example.chitwing.anycure_kotlin_master.ot

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.model.RecipeSection

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/8/21
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class RecipeSectionAdapter(private val data:List<RecipeSection>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){




    override fun getItemCount(): Int {
        return  data.count()
    }

    /**
     * var type:Int = 0x01
     * 0x01 -> header
     * 0x02 -> 普通itemView
     * 0x03 -> 精品itemView
     * */
    override fun getItemViewType(position: Int): Int {
        return data[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0x01 -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recipe_section_header, parent, false)
                return RecipeSectionAdapter.HeaderView(v)
            }
            0x02 -> {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.ot_recipe_boutique_item, parent, false)
                return RecipeSectionAdapter.BoutiqueItemView(v)
            }
            0x03 -> {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.ot_recipe_normal_item, parent, false)
                return RecipeSectionAdapter.NormalItemView(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.ot_recipe_item, parent, false)
                return RecipeSectionAdapter.HeaderView(v)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val obj = data[position]
        when(holder.itemViewType){
            0x01 -> {
                holder as RecipeSectionAdapter.HeaderView
                holder.title.text = obj.name
            }
            0x02 -> {

            }
            0x03 -> {}
            else -> {}
        }
    }



    /**
     * 页头
     * */
    class HeaderView(v:View):RecyclerView.ViewHolder(v){
        val title :TextView = v.findViewById(R.id.recipe_section_header_title)
        val button :Button = v.findViewById(R.id.recipe_section_header_button)
    }

    /**
     * 普通item
     * */
    class NormalItemView(v: View):RecyclerView.ViewHolder(v){
        val recylerView:RecyclerView = v.findViewById(R.id.ot_recipe_normal_item_recycler_view)
    }

    /**
     * 精品item
     * */
    class BoutiqueItemView(v: View):RecyclerView.ViewHolder(v){
        val recylerView:RecyclerView = v.findViewById(R.id.ot_recipe_boutique_item_recycler_view)
    }



}