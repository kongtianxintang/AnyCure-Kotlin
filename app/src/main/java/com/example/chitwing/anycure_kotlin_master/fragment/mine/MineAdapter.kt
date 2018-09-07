package com.example.chitwing.anycure_kotlin_master.fragment.mine

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.about.AboutActivity
import com.example.chitwing.anycure_kotlin_master.activity.bind.BindActivity
import com.example.chitwing.anycure_kotlin_master.model.MineModel
import com.example.chitwing.anycure_kotlin_master.unit.showToast

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/28
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class MineAdapter(private val mDataSet:List<MineModel>,private val context: MineFragment):RecyclerView.Adapter<MineAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.mine_item, parent, false)
        return MineAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataSet[position]
        holder.mTextView.text = item.title
        holder.itemView.setOnClickListener {
            when(position){
                0 -> {
                    val intent = Intent(context.activity!!,BindActivity ::class.java)
                    context.activity!!.startActivity(intent)
                }
                2 -> {
                    val intent = Intent(context.activity!!, AboutActivity ::class.java)
                    context.activity!!.startActivity(intent)
                }
                else -> {
                    context.activity?.showToast("功能暂未开放")
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return mDataSet.size
    }


    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val mTextView: TextView = v.findViewById(R.id.mine_item_title)
    }
}

class OtMineAdapter( mDataSet:List<MineModel>,layoutResId: Int) : BaseQuickAdapter<MineModel, BaseViewHolder>(layoutResId,mDataSet){

    override fun convert(helper: BaseViewHolder?, item: MineModel?) {
        helper?.setText(R.id.mine_item_title,item?.title)
    }
}