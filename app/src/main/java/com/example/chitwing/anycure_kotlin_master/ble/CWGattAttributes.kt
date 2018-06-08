package com.example.chitwing.anycure_kotlin_master.ble

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
        val sUUID = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E"
        /**
         * 广播uuid
         */
        val notifyUUID =  "6E400003-B5A3-F393-E0A9-E50E24DCCA9E"
        /**
         * 写入uuid
         * */
        val writeUUID = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E"
    }

}