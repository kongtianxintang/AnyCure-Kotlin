package com.example.chitwing.anycure_kotlin_master.unit

import android.content.Context
import android.content.SharedPreferences
import com.example.chitwing.anycure_kotlin_master.app.MyApp
import java.util.*

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/9/25
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
object  SharedPreferencesHelper {

    //禁忌事项提示标示
    const val hintKey = "hintKey"
    //存储手机号码
    const val telephone = "phone"

    /**
     * 获取
     * */
    private fun getShared(): SharedPreferences{
        val app = MyApp.getApp()
        return app.getSharedPreferences(app.packageName,Context.MODE_PRIVATE)
    }

    /**
     * 保存
     * */
    fun put(key: String,obj: Any){
        val shared = getShared()
        val editor = shared.edit()
        when (obj){
            is String -> editor.putString(key,obj)
            is Int -> editor.putInt(key,obj)
            is Float -> editor.putFloat(key,obj)
            is Boolean -> editor.putBoolean(key,obj)
            is Long -> editor.putLong(key,obj)
            else -> editor.putString(key,obj.toString())
        }
        editor.apply()
    }

    /**
     * 获取键值
     * */
    fun getObject(key: String,default: Any): Any{
        val shared = getShared()
        return when(default){
            is String ->  shared.getString(key,default)
            is Boolean -> shared.getBoolean(key,default)
            is Int -> shared.getInt(key,default)
            is Float -> shared.getFloat(key,default)
            is Long -> shared.getLong(key,default)
            else ->  shared.getString(key,default.toString())
        }
    }

    /**
     * 清除对应键值对
     * */
    fun remove(key: String): Boolean{
        val shared = getShared()
        val edit = shared.edit()
        edit.remove(key)
        return edit.commit()
    }

    /**
     * 清除所有数据
     * */
    fun clear(): Boolean{
        val shared = getShared()
        val edit = shared.edit()
        edit.clear()
        return edit.commit()
    }


}