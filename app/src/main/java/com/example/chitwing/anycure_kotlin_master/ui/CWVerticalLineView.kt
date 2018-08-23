package com.example.chitwing.anycure_kotlin_master.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.ViewCompat.setLayerType
import android.util.AttributeSet
import android.view.View
import com.example.chitwing.anycure_kotlin_master.unit.Unit

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/8/23
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWVerticalLineView :View{

    /**
     * 画笔 -实 轨道部分
     * */
    private val mPaint by lazy {
        return@lazy createPaint()
    }

    /**
     * 创建画笔
     * */
    private fun createPaint() : Paint {
        val m = Paint()
//        m.isAntiAlias = true
        m.color = Color.RED
        m.style = Paint.Style.STROKE
        m.strokeCap = Paint.Cap.ROUND
        return m
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        defaultConfigure()
    }

    private fun defaultConfigure(){

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val sX:Float = 0.toFloat()
        val sY:Float = 0.toFloat()
        val eX = 0.toFloat()
        val eY = height.toFloat()
        mPaint.strokeWidth = 25.toFloat()

        canvas?.drawLine(sX,sY,eX,eY,mPaint)
    }


}