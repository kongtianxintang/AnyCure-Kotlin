package com.example.chitwing.anycure_kotlin_master.ui

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/9/3
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWLayoutManager(val context:Context, orientation:Int,
                       reverseLayout:Boolean) : LinearLayoutManager(context,orientation,reverseLayout){

    private val mTag = "自定义Layout"
    private var mHorizontallyOffset:Int = 0
    private var mTotalWidth:Int = 0
    private val mSpace:Int = 24
    private var mCurrentIndex:Int? = null//当前的item


    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        detachAndScrapAttachedViews(recycler)//分离所有的itemView

        if (itemCount == 0 ){return}

        mHorizontallyOffset = 0
        var offsetX = 0
        val offsetY = 0
        var lastSpace = 0

        for (i in 1 .. itemCount){
            val position = i - 1
            val scrap = recycler?.getViewForPosition(position)
            addView(scrap)
            measureChild(scrap,0,0)

            val itemWidth = getDecoratedMeasuredWidth(scrap)
            val itemHeight = getDecoratedMeasuredHeight(scrap)
            if (position == 0){
                offsetX = (width - itemWidth) / 2
            }
            if (position == itemCount - 1){
                lastSpace = (width - itemWidth) / 2
            }
            val x = offsetX + itemWidth
            val y = offsetY + itemHeight

            layoutDecorated(scrap,offsetX,offsetY,x,y)

            offsetX += itemWidth + mSpace + lastSpace
        }
        calculateItemScale(recycler,state)
        mTotalWidth = offsetX
    }

    /**
     * 计算缩放度
     * */
    private fun calculateItemScale(recycler: RecyclerView.Recycler?, state: RecyclerView.State?){
        /**
         * 显示的区域
         * */
        val displayFrame = Rect(mHorizontallyOffset,0,mHorizontallyOffset + width ,height)
        val pCenterX = width / 2

        val minScale = 0.6f
        val maxScale = 1f
        val maxMinDiff = maxScale - minScale
        val minAlpha = 0.0f
        val maxAlpha = 1f
        val maxMinDiffAlpha = maxAlpha - minAlpha

        for (i in 1 .. itemCount){
            val index = i - 1
            val view = getChildAt(index)
            view?.let {
                val centerX = (it.left + it.right ) / 2
                val distanceFromCenter = centerX - pCenterX
                val position = Math.min(Math.max(-1f, distanceFromCenter / pCenterX.toFloat()), 1f)
                val closenessToCenter = 1f - Math.abs(position)
                val scale = minScale + maxMinDiff * closenessToCenter
                val alpha = minAlpha + maxMinDiffAlpha * closenessToCenter
                it.scaleX = scale
                it.scaleY = scale
                it.alpha = alpha
                if (scale in 0.95 .. 1.0){
                    if (mCurrentIndex != index){
                        mCurrentIndex = index
                        Log.d(mTag,"当前item->$mCurrentIndex")
                    }
                }
            }
        }
    }


    /**水平方向*/
    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        var travel = dx
        if (mHorizontallyOffset + dx < 0){
            travel = -mHorizontallyOffset
        }else if (mHorizontallyOffset + dx > mTotalWidth - getHorizontallySpace()){
            travel = mTotalWidth - getHorizontallySpace() - mHorizontallyOffset
        }

        mHorizontallyOffset += travel

        offsetChildrenHorizontal(-travel)

        calculateItemScale(recycler,state)

        return travel
    }

    private fun getHorizontallySpace(): Int{
        return width - paddingLeft - paddingRight
    }

    /**垂直方向*/
    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        when (state){
            RecyclerView.SCROLL_STATE_IDLE -> { Log.d(mTag,"SCROLL_STATE_IDLE") }
            RecyclerView.SCROLL_STATE_SETTLING -> { Log.d(mTag,"SCROLL_STATE_SETTLING") }
            RecyclerView.SCROLL_STATE_DRAGGING -> { Log.d(mTag,"SCROLL_STATE_DRAGGING") }
        }
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}