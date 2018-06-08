package com.example.chitwing.anycure_kotlin_master.database

import android.app.Activity
import android.content.Context
import com.example.chitwing.anycure_kotlin_master.app.MyApp
import com.example.chitwing.anycure_kotlin_master.model.BaseData_
import io.objectbox.Box

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/5
 * QQ/Tel/Mail:383118832
 * Description:数据库操作
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
object DBHelper {

    /*查找 单个id*/
    fun <T>find(id:Long, context: Context, entityClass:Class<T>):T?{
        val logs = syncLogs(context,entityClass)
        return logs.get(id)
    }
    /*查找 数组id*/
    fun <T>find(list:List<Long>, context: Context, entityClass:Class<T>):List<T>?{
        val logs = syncLogs(context,entityClass)
        return logs.get(list)
    }
    /*查找 所有*/
    fun <T>findAll(context: Context, entityClass:Class<T>) :List<T>?{
        val logs = syncLogs(context,entityClass)
        val query = logs.query().order(BaseData_.id).build()
        return query.find()
    }
    /*插入 单个*/
    fun <T>insert(context: Context, entity: T, entityClass: Class<T>){
        val logs = syncLogs(context,entityClass)
        logs.put(entity)
    }
    /*插入 数组*/
    fun <T>insert(context: Context, list: List<T>, entityClass: Class<T>){
        val logs = syncLogs(context,entityClass)
        logs.put(list)
    }
    /*更新*/
    fun <T>update(context: Context, entity: T, entityClass: Class<T>){
        val logs = syncLogs(context,entityClass)
        logs.put(entity)
    }
    /*删除:有目标--根据id*/
    fun <T>remove(context: Context, id:Long, entityClass: Class<T>){
        val logs = syncLogs(context,entityClass)
        logs.remove(id);
    }
    /*删除:单个*/
    fun <T>remove(context: Context, entity: T, entityClass: Class<T>){
        val logs = syncLogs(context,entityClass)
        logs.remove(entity)
    }
    /*删除:所有*/
    fun <T>removeAll(context: Context, entityClass: Class<T>){
        val logs = syncLogs(context,entityClass)
        logs.removeAll()
    }
    /*获取box*/
    private fun <T>syncLogs(context: Context, entityClass: Class<T>) : Box<T> {
        val ac = (context as Activity).application
        val store = (ac as MyApp).boxStore
        return store.boxFor(entityClass)
    }

}