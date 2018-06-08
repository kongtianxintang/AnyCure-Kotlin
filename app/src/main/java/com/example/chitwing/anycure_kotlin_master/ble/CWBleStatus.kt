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
    Support,//支持蓝牙
    DisSupport,//不支持蓝牙
    Disable,//不能使用
    Able,//可以使用
    Discover,//发现服务
    BeginScan,//扫描
    StopScan//停止扫描
}