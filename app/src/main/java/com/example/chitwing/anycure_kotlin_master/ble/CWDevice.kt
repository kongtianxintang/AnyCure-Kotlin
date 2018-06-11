package com.example.chitwing.anycure_kotlin_master.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.util.Log

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/8
 * QQ/Tel/Mail:383118832
 * Description:外接设备类 处理各种情况
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
data class CWDevice (val device:BluetoothDevice,val gatt:BluetoothGatt):CWGattReadInterface,CWGattWriteInterface{

    private val tag = "CWDevice"

    /**
     * 读取类
     * */
    val gattRead:CWGattRead by lazy {
        return@lazy CWGattRead(this)
    }
    /**
     * 写入类
     * */
    val gattWrite:CWGattWrite by lazy {
        return@lazy CWGattWrite(this)
    }

    fun removeSelf(){
        val isRemove = CWBleManager.mCWDevices.remove(this)
        Log.d(tag,"删除外接设备成功与否:$isRemove")
    }



    /**********************  CWGattReadInterface  start  **************************/
    override fun cwBleBatteryPower(value: Int) {
        Log.d(tag,"电池电量:$value")
    }

    override fun cwBleRecipeLoadingCallback(flag: Boolean, duration: Int) {
        Log.d(tag,"处方加载指令 成功与否:$flag,处方时间:$duration")
    }

    override fun cwBleRecipeSendIndexCallback(index: Int, total: Int) {
        Log.d(tag,"处方帧数:$index,已发送帧数:$total")
    }

    override fun cwBleSoftwareStartCureCallback(flag: Boolean) {
        Log.d(tag,"开始输出:$flag")
    }

    override fun cwBleSoftwareStopCureCallback(flag: Boolean) {
        Log.d(tag,"停止输出:$flag")
    }
    /**********************  CWGattReadInterface  end  **************************/


    /**********************  CWGattWriteInterface start  ************************/
    override fun cwGattWriteData(list: ByteArray) {
        val service = gatt.getService(CWGattAttributes.CW_SERVICE_UUID)
        val char = service.getCharacteristic(CWGattAttributes.CW_CHARACTER_writeUUID)
        char.value = list
        char.writeType
        Log.d(tag,"写入渠道号")
        list.forEach { Log.d(tag,"数值:$it") }
        gatt.writeCharacteristic(char)
    }
    /**********************  CWGattWriteInterface end  ************************/


    /**
     * 开始往设备写入数据
     * */
    fun writeData(){
        gattWrite.cwBleWriteChannelCode(CWBleManager.configure.channel.encrypt_Channel_Code)
    }
}


