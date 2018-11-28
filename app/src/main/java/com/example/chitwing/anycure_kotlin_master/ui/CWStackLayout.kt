package com.example.chitwing.anycure_kotlin_master.ui

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:liwei
 * Date:  2018/11/22
 * QQ/Tel/Mail:383118832
 * Description: 类似于iOS UIStackView 布局   平分整个屏幕 居中
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWStackLayout: RecyclerView.LayoutManager() {


    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        detachAndScrapAttachedViews(recycler)
        if (itemCount == 0 ){
            removeAndRecycleAllViews(recycler)
            return
        }

        /**
         * 取中间值
         * 然后以中心为锚点 左右铺开
         * */
        layoutCenterItem(recycler)
        
    }

    /**
     * 布局中间的item
     * */
    private fun layoutCenterItem(recycler: RecyclerView.Recycler?){
        val t = itemCount % 2 == 0
        val centerX = width / 2

        if (t) {
            //双数
            val center = itemCount / 2
            layoutLeftItems(recycler,centerX,center)
            layoutRightItems(recycler,centerX,center)
        }else{
            //单数
            val center = itemCount / 2
            val item = recycler?.getViewForPosition(center)
            item?.let {
                addView(it)
                measureChild(it,0,0)
                val itWidth = getDecoratedMeasuredWidth(it)
                val itHeigt = getDecoratedMeasuredWidth(it)
                val left = centerX - itWidth / 2
                val right = centerX + itWidth / 2
                layoutDecorated(it,left,0,right,itHeigt)

                layoutLeftItems(recycler,left,center )
                layoutRightItems(recycler,right,center + 1)
            }
        }
    }


    /**
     * 左边 items 布局
     * */
    private fun layoutLeftItems(recycler: RecyclerView.Recycler?,startX:Int,index:Int){
        var leftIndex = index
        var endX = startX
        while (leftIndex > 0){
            leftIndex -= 1
            val item = recycler?.getViewForPosition(leftIndex)
            item?.let {
                addView(it)
                measureChild(it,0,0)
                val itemWidth = getDecoratedMeasuredWidth(it)
                val itemHeight = getDecoratedMeasuredHeight(it)
                val top = 0
                val right = endX
                val left = endX - itemWidth
                layoutDecorated(it,left,top,right,itemHeight)
                endX = left
            }
        }
    }

    /**
     * 右边 items 布局
     * */
    private fun layoutRightItems(recycler: RecyclerView.Recycler?,startX:Int,index:Int){
        var rightIndex = index - 1
        val max = itemCount - 1
        var endX = startX
        while (rightIndex < max){
            rightIndex += 1
            val item = recycler?.getViewForPosition(rightIndex)
            item?.let {
                addView(it)
                measureChild(it,0,0)
                val itemWidth = getDecoratedMeasuredWidth(it)
                val itemHeight = getDecoratedMeasuredHeight(it)
                val top = 0
                val left = endX
                val right = endX + itemWidth
                layoutDecorated(it,left,top,right,itemHeight)
                endX = right
            }
        }
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}