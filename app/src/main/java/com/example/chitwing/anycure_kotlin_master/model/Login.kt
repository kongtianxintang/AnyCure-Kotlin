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
data class Login(var code:Int,
                 var msg:String?,
                 var uid:String?,
                 var u_type:String?,
                 var icon:String?) :BaseData()
{

}