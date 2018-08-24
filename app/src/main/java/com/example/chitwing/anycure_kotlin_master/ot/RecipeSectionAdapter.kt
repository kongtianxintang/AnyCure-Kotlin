package com.example.chitwing.anycure_kotlin_master.ot

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.model.Recipe
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
class RecipeSectionAdapter(val context:Context,val data:List<RecipeSection>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    var callback:RecipeInterface? = null

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

    private val mBoutiqueCallback = object :RecipeInterface {
        override fun didSelectItem(obj: Recipe) {
            if (callback != null){
                callback!!.didSelectItem(obj)
            }
        }
    }
    private val mNormalCallback = object :RecipeInterface {
        override fun didSelectItem(obj: Recipe) {
            if (callback != null){
                callback!!.didSelectItem(obj)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0x01 -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recipe_section_header, parent, false)
                return RecipeSectionAdapter.SectionHeaderView(v)
            }
            0x02 -> {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.ot_recipe_boutique_item, parent, false)

                val gridLayout = GridLayoutManager(parent.context,2,LinearLayoutManager.HORIZONTAL,false)
                val t = RecipeSectionAdapter.BoutiqueItemView(v)
                t.recyclerView?.layoutManager = gridLayout
                t.adapter = RecipeBoutiqueAdapter(parent.context)
                t.adapter?.callback = mBoutiqueCallback
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
                t.adapter?.callback = mNormalCallback
                t.recyclerView?.adapter = t.adapter
                return t
            }
            else -> {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.recipe_header_view, parent, false)
                val holder = RecipeSectionAdapter.HeaderView(v)
                holder.itemView.setOnClickListener {
                    Log.d("页头","banner")
                    if (data.count() > 2){
                        val list = data[2].data
                        val first = list?.firstOrNull()
                        first?.let {
                            if (callback != null){
                                callback!!.didSelectItem(it)
                            }
                        }
                    }
                }
                return holder
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val obj = data[position]
        when(holder.itemViewType){
            0x01 -> {
                holder as RecipeSectionAdapter.SectionHeaderView
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
    class SectionHeaderView(v:View):RecyclerView.ViewHolder(v){
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
    /**
     * header
     * */
    class HeaderView(v: View) :RecyclerView.ViewHolder(v){
        val imageView:ImageView = v.findViewById(R.id.recipe_header_view)
    }



}