package com.example.chitwing.anycure_kotlin_master.fragment.cure

import android.animation.AnimatorInflater
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.ble.CWDevice
import java.net.URI

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/7/16
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CureAdapter(private val mContext:CureFragment,private val dataSet:List<CWDevice>) :RecyclerView.Adapter<CureAdapter.ViewHolder>(),View.OnClickListener{


    /**
     * 所选择的位置
     * */
    private var mSelect:Int = 0
    fun setSelect(arg:Int){
        if (arg != mSelect){
            mSelect = arg
            resetCallback()
        }
    }
    /**
     * 获取选择的设备
     * */
    fun getSelectItem():CWDevice?{
        if (dataSet.count() > mSelect){
            return dataSet[mSelect]
        }
        return null
    }

    fun resetCallback(){
        val current = getSelectItem()
        current?.let {
            dataSet.forEach {
                if (current.mDevice.address == it.mDevice.address) {
                    it.mCallback = mContext.provider.callback
                    Log.e("测试","设置回调->${it.mDevice.address}")
                    it.isSelect = true
                    it.selectDevice(true)
                    mContext.switchDevice(it)
                } else {
                    it.mCallback = null
                    it.isSelect = false
                    it.selectDevice(false)
                    Log.e("测试","回调置空 ->${it.mDevice.address}")
                }
            }
        }
    }
    private val mAnimator by lazy {
        return@lazy AnimatorInflater.loadAnimator(mContext.activity!!,R.animator.alpha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.cure_item, parent, false)
        v.setOnClickListener(this)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tag = position
        val item = dataSet[position]
        holder.name.text = item.recipe?.recipeName
        val name = "part_${item.recipe!!.recipeId}"
        val resId = mContext.activity!!.resources.getIdentifier(name,"mipmap",mContext.activity!!.packageName)
        holder.icon.setImageResource(resId)

        if (item.isSelect){
            mAnimator.setTarget(holder.line)
            mAnimator.start()
        }else{
            holder.line.clearAnimation()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.count()
    }


    override fun onClick(v: View?) {
        v?.let {
            val arg = it.tag as Int
            setSelect(arg)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.cure_item_icon)
        val name: TextView = v.findViewById(R.id.cure_item_name)
        val line: View = v.findViewById(R.id.cure_item_line)
    }
}