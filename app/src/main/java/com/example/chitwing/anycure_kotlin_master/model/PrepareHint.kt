package com.example.chitwing.anycure_kotlin_master.model

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.unit.Unit
import io.objectbox.annotation.Entity

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/27
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
@Entity
class PrepareHint :BaseData(){
    var title:String? = null
    var content:String? = null

    /**
     * 富文本
     * */
    fun getSpan() :SpannableStringBuilder?{

        val temp = title ?: "默认"
        val desc =  "\n" + (content ?:"默认")
        val color = ForegroundColorSpan(Color.BLUE)
        val size = AbsoluteSizeSpan(Unit.dip2px(20f))

        val spanBuilder = SpannableStringBuilder(temp)

        spanBuilder.setSpan(size,0,temp.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanBuilder.setSpan(color,0,temp.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spanBuilder.append(desc)

        val normal = ForegroundColorSpan(Color.RED)
        val normalSize = AbsoluteSizeSpan(Unit.dip2px(17f))
        spanBuilder.setSpan(normal,temp.length,spanBuilder.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanBuilder.setSpan(normalSize,temp.length,spanBuilder.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spanBuilder
    }

    /**
     * 获取替换掉的字符串
     * */
    fun getReplaceContent() :String?{
        val temp = content ?: "默认"
        val r = temp.replace("\\r","")
        return r.replace("\\n","\n")
    }

}