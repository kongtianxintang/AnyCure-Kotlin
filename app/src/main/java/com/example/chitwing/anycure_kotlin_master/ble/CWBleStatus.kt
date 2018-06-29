package com.example.chitwing.anycure_kotlin_master.ble

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/7
 * QQ/Tel/Mail:383118832
 * Description:蓝牙的状态
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
enum class CWBleStatus{
    ON,//打开蓝牙
    OFF,//关闭蓝牙
    Support,//支持蓝牙
    DisSupport,//不支持蓝牙
    Disable,//不能使用
    Able,//可以使用
    Discover,//发现服务
    Connect,//链接
    Disconnect,//断开链接
    BeginScan,//扫描
    StopScan;//停止扫描

    /**
     * 描述
     * */
    val desc by lazy {
        when(this){
            ON -> return@lazy "打开蓝牙"
            OFF -> return@lazy "关闭蓝牙"
            Support -> return@lazy "支持蓝牙"
            DisSupport -> return@lazy "不支持蓝牙"
            Disable -> return@lazy  "不能使用"
            Able -> return@lazy "可以使用"
            Discover -> return@lazy "发现服务"
            Connect -> return@lazy "链接设备成功"
            Disconnect -> return@lazy "断开链接"
            BeginScan -> return@lazy "开始扫描"
            else -> return@lazy "停止扫描"
        }
    }
}
