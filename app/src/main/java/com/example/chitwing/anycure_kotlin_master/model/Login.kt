package com.example.chitwing.anycure_kotlin_master.model

import io.objectbox.annotation.Entity

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/5
 * QQ/Tel/Mail:383118832
 * Description:登录
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
@Entity
class Login :BaseData()
{
    var code:Int = 0
    var msg:String? = null
    var uid:String? = null
    var u_type:String? = null
    var icon:String? = null

}