package com.example.chitwing.anycure_kotlin_master.ble

import android.bluetooth.BluetoothDevice

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/22
 * QQ/Tel/Mail:383118832
 * Description:扫描设备回调
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
interface CWScanCallback {
    /**
     * 发现的设备
     * */
    fun discoveryDevice(item: BluetoothDevice,manager:CWBleManager)

}