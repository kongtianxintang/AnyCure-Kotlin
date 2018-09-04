package com.example.chitwing.anycure_kotlin_master.fragment.otCure

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.ble.CWDevice
import com.example.chitwing.anycure_kotlin_master.model.Recipe
import kotlinx.android.synthetic.main.ot_cure_item.view.*

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/8/15
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class OtCureAdapter(layoutResId: Int,data:List<CWDevice>) :BaseQuickAdapter<CWDevice,BaseViewHolder>(layoutResId,data){


    override fun convert(helper: BaseViewHolder?, item: CWDevice?) {
        helper?.setText(R.id.otCureRecipeName,item?.recipe?.recipeName)
        item?.let {
            when (it.isSelect){
                true -> {
                    helper?.setAlpha(R.id.otCureRecipeName,1f)
                }
                else -> {
                    helper?.setAlpha(R.id.otCureRecipeName,0.3f)
                }
            }
        }
    }



}