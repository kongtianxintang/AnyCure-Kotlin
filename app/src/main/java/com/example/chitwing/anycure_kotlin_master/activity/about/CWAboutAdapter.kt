package com.example.chitwing.anycure_kotlin_master.activity.about

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.model.AboutBean

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/10/23
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWAboutAdapter( mDataSet:List<AboutBean>,layoutResId: Int) : BaseQuickAdapter<AboutBean, BaseViewHolder>(layoutResId,mDataSet){

    override fun convert(helper: BaseViewHolder?, item: AboutBean?) {
        helper?.setText(R.id.mItemTitle,item?.title)
        helper?.setText(R.id.mItemDesc,item?.desc)
    }

}