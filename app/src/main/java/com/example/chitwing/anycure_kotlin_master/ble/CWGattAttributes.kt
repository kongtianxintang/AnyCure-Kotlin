package com.example.chitwing.anycure_kotlin_master.ble

import java.util.*

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/8
 * QQ/Tel/Mail:383118832
 * Description:存放 服务id
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWGattAttributes {

    companion object {
        /**
         * 服务uuid
         */
        val CW_SERVICE_UUID:UUID? = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")
        /**
         * 广播uuid
         */
        val CW_notifyUUID:UUID? = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E")
        /**
         * 写入uuid 特征值
         * */
        val CW_CHARACTER_writeUUID:UUID? = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E")
        /**
         * 广播包里的uuid
         * */
        val Cw_Service_Data = UUID.fromString("00005801-0000-1000-8000-00805f9b34fb")

    }


}