package com.example.chitwing.anycure_kotlin_master.ui

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.unit.Unit

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/9/17
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWPointAnimation: View {

    private val mTag = "CWPointAnimation"

    private val mLeftPaint by lazy {
        return@lazy createPaint()
    }

    private val mCenterPaint by lazy {
        return@lazy createPaint()
    }

    private val mRightPaint by lazy {
        return@lazy createPaint()
    }

    private fun createPaint(): Paint{
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = resources.getColor(R.color.main)
        return p
    }

    /**
     * 在代码中创建调用
     * */
    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    /**
     * 在xml中创建调用
     * */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    /**
     * 在xml中创建调用
     * */
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //1.画三个点
        //  left - center - right

        val x = (width / 2).toFloat()
        val y = (height / 2).toFloat()
        val px = Unit.dip2px(4f).toFloat()
        val space = Unit.dip2px(4f).toFloat()
        canvas?.drawCircle(x,y,px,mCenterPaint)

        val leftX = x - 3 * px - space / 2
        canvas?.drawCircle(leftX,y,px,mLeftPaint)

        val rightX = x + 3 * px + space / 2
        canvas?.drawCircle(rightX,y,px,mRightPaint)
    }


    /**
     * 开始动画
     * */
    fun startAlphaAnimation(){
        val ani = ScaleAnimation(1f,0f,1f,0f)
        ani.fillBefore = true
        ani.duration = 1000
        ani.repeatCount = -1
        ani.repeatMode = Animation.REVERSE
        ani.start()
        this.animation = ani
    }

    /**
     * 结束动画
     * */
    fun endAlphaAnimation(){

    }

}