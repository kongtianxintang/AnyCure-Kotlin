package com.example.chitwing.anycure_kotlin_master.ble

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/11
 * QQ/Tel/Mail:383118832
 * Description:数据写入类
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWGattWrite(b:CWGattWriteInterface) :CWGattWriteInterface by b {

    /**
     * 写入渠道号
     * */
    fun cwBleWriteChannelCode(codes:List<Int>){
        val maps = codes.map { it.toByte() }
        val list = mutableListOf<Byte>()
        val f = 0xce
        val s = 0x11
        list.add(f.toByte())
        list.add(s.toByte())
        list.addAll(maps)
        cwGattWriteData(list.toByteArray())
    }
}