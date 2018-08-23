package com.example.chitwing.anycure_kotlin_master.ot

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
class RecipeSectionAdapter(val context:Context,private val data:List<RecipeSection>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){




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

    val lookup = object :GridLayoutManager.SpanSizeLookup(){
        override fun getSpanSize(position: Int): Int {
            return 5
        }
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

                val gridLayout = GridLayoutManager(parent.context,2,LinearLayoutManager.HORIZONTAL,false)
                val t = RecipeSectionAdapter.BoutiqueItemView(v)
                t.recyclerView?.layoutManager = gridLayout
                t.adapter = RecipeBoutiqueAdapter(parent.context)
                t.recyclerView?.adapter = t.adapter
                return t
            }
            0x03 -> {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.ot_recipe_normal_item, parent, false)
                val t = RecipeSectionAdapter.NormalItemView(v)
                val layoutManager = LinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL,false)
                t.recyclerView?.layoutManager = layoutManager
                t.adapter = RecipeNormalAdapter(parent.context)
                t.recyclerView?.adapter = t.adapter
                return t
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
                holder as RecipeSectionAdapter.BoutiqueItemView
                holder.adapter?.setDataSource(obj.data)
            }
            0x03 -> {
                holder as RecipeSectionAdapter.NormalItemView
                holder.adapter?.setDataSource(obj.data)
            }
            else -> {

            }
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
        var recyclerView:RecyclerView? = null
        var adapter:RecipeNormalAdapter? = null
        init {
            recyclerView = v.findViewById(R.id.ot_recipe_normal_item_recycler_view)
        }
    }

    /**
     * 精品item
     * */
    class BoutiqueItemView(v: View):RecyclerView.ViewHolder(v){
        var recyclerView:RecyclerView? = null
        var adapter:RecipeBoutiqueAdapter? = null
        init {
            recyclerView = v.findViewById(R.id.ot_recipe_boutique_item_recycler_view)
        }
    }



}