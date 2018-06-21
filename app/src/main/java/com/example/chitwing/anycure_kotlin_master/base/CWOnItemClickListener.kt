package com.example.chitwing.anycure_kotlin_master.base

import android.view.View

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/21
 * QQ/Tel/Mail:383118832
 * Description:RecyclerView的item点击事件
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
interface CWOnItemClickListener {
    /**
     * view 点击的
     * position 位置
     * */
    fun onItemClick(view: View,position:Int)
}