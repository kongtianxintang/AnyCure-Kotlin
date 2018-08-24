package com.example.chitwing.anycure_kotlin_master.ot

import com.example.chitwing.anycure_kotlin_master.model.Recipe

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

/**
 * 定义一个类似与iOS中IndexPath的类
 * section :在第几区域
 * row:在当前区域的第几个
 * */
data class IndexPath(val section:Int,val row:Int)

interface RecipeInterface {
    fun didSelectItem(obj:Recipe)
}

