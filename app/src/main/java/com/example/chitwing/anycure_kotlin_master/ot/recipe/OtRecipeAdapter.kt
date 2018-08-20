package com.example.chitwing.anycure_kotlin_master.ot.recipe

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/8/16
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class OtRecipeAdapter:SectionedRecyclerViewAdapter<OtRecipeAdapter.HeaderHolder, OtRecipeAdapter.ItemHolder, OtRecipeAdapter.FooterHolder>() {


    override fun getItemCountForSection(section: Int): Int {
        return section + 1
    }

    override fun getSectionCount(): Int {
        return 5
    }

    override fun hasFooterInSection(section: Int): Boolean {
        return false
    }

    private fun getLayoutInflater(arg:Context) :LayoutInflater {
        return LayoutInflater.from(arg)
    }

    override fun onCreateSectionHeaderViewHolder(parent: ViewGroup?, viewType: Int): HeaderHolder {
        val view = getLayoutInflater(parent!!.context).inflate(R.layout.recipe_header_view,parent,false)
        return HeaderHolder(view)
    }

    override fun onCreateSectionFooterViewHolder(parent: ViewGroup?, viewType: Int): FooterHolder {
        val view = getLayoutInflater(parent!!.context).inflate(R.layout.recipe_header_view,parent,false)
        return FooterHolder(view)
    }

    override fun onCreateItemViewHolder(parent: ViewGroup?, viewType: Int): ItemHolder {
        val view = getLayoutInflater(parent!!.context).inflate(R.layout.ot_recipe_item,parent,false)
        return ItemHolder(view)
    }

    override fun onBindItemViewHolder(holder: ItemHolder?, section: Int, position: Int) {
        //
    }

    override fun onBindSectionHeaderViewHolder(holder: HeaderHolder?, section: Int) {
        holder?.textView?.text = "测试"
    }

    override fun onBindSectionFooterViewHolder(holder: FooterHolder?, section: Int) {

    }

    class HeaderHolder(v:View) : RecyclerView.ViewHolder(v){
        val textView:TextView = v.findViewById(R.id.recipe_header_text_view)
    }

    class FooterHolder(v:View) :RecyclerView.ViewHolder(v){
        
    }

    class ItemHolder(v: View) :RecyclerView.ViewHolder(v){
        val recyclerView:RecyclerView = v.findViewById(R.id.ot_recipe_item_recycler_view)
    }
}