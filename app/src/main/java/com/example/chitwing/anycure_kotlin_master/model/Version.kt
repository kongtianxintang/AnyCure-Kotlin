package com.example.chitwing.anycure_kotlin_master.model

import io.objectbox.annotation.Entity

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/9/27
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
@Entity
class Version :BaseData(){
    var code:Int = 0
    var version: String? = null
    var content: String? = null
    var name: String? = null
    var size: String? = null
    var url: String? = null
}