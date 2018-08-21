package com.example.chitwing.anycure_kotlin_master.model

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/8/20
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class RecipeSection {
    var isHead:Boolean = false
    var name:String? = null
    var isMore:Boolean = false
    var data:List<Recipe>? = null
    var type:Int = 0x01

    constructor(isHead:Boolean,name:String,isMore:Boolean){
        this.isHead = isHead
        this.name = name
        this.isMore = isMore
    }

    constructor(type:Int,data:List<Recipe>){
        this.data = data
        this.type = type
    }

}