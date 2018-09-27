package com.example.chitwing.anycure_kotlin_master.unit

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/9/14
 * QQ/Tel/Mail:383118832
 * Description:正则
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
enum class CWRegex {
    Email,
    Phone,
    Password,
    Nickname,
    Username;

    /**
     * 判断是否正确
     * */
    fun isRight(arg: String): Boolean{
       return when (this){
            Email -> {
                val pattern = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$"
                Regex(pattern).matches(arg)
            }
            Phone -> {
                val pattern = "^((13[0-9])|(15[0,0-9])|(17[0,0-9])|(18[0,0-9]))\\d{8}$"
                Regex(pattern).matches(arg)
            }
            Password -> {
                val pattern = "^[a-zA-Z0-9]{6,16}+$"
                Regex(pattern).matches(arg)
            }
            else -> { false }
        }
    }

}