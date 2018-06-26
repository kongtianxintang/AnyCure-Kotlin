package com.example.chitwing.anycure_kotlin_master.ble

import android.bluetooth.BluetoothDevice
import android.util.Log

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/11
 * QQ/Tel/Mail:383118832
 * Description:扩展
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
enum class CWDeviceType{
    New,
    Old
}
/**
 * 扩展设备是否为新设备
 * */
fun BluetoothDevice.deviceType() :CWDeviceType {
    if (name.contains("med_")){
        return CWDeviceType.New
    }
    return CWDeviceType.Old
}

/**
 * 扩展 获取设备的渠道号
 * */
fun BluetoothDevice.channelCode() :String?{
    val type = deviceType()
    when(type){
        CWDeviceType.Old -> {
            return null
        }
        else -> {
            if (name.length >= 8) {
                return name.substring(4,8)
            }
            return null
        }
    }
}