package com.example.chitwing.anycure_kotlin_master.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.os.ParcelUuid
import android.util.Log
import java.text.DecimalFormat

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
    if (name.contains("MRB_")){
        return CWDeviceType.Old
    }
    return CWDeviceType.New
}

/**
 * 扩展 获取设备的渠道号
 * */
fun BluetoothDevice.channelCode() :String?{
    val type = deviceType()
    when(type){
        CWDeviceType.New -> {
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

/**
 * 从广播包里获取渠道号
 * */
val ScanResult.channelCode: String?
    get():String?{
        val pUuid = ParcelUuid(CWGattAttributes.Cw_Service_Data)
        val data = this.scanRecord.getServiceData(pUuid)
        val subs = data.map { val t = it.toInt()
            if (t !in 0 .. 255){
                t and 0xff
            }else{
                t
            }
        }
        var code:String? = null
        //todo: 隐患 担心到了10以后咋整
        val format = DecimalFormat("00")
        if (subs.count() > 1){
            for (i in 0 until 2){
                val t = subs[i]
                if (code == null){
                    code = format.format(t)
                }else {
                    code += format.format(t)
                }
            }
        }
        return code
    }
