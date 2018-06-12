package com.example.chitwing.anycure_kotlin_master.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
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
     * 处方的时间
     * */
    var mDuration:Int = 1200

    private val mRecipeContent = listOf(208,7,2,20,152,58,0,250,136,19,21,46,208,7,2,3,136,19,11,215,220,5,11,66,208,7,2,5,184,11,4,80,232,3,4,30,208,7,2,10,208,7,11,35,111,0,17,1,208,7,2,10,220,5,4,14,60,0,11,1,208,7,2,10,208,7,21,20,60,0,4,1,208,7,2,8,220,5,11,15,36,0,11,1,208,7,2,14,184,11,4,27,111,0,17,1,208,7,2,8,16,39,4,170,184,11,4,50,208,7,2,14,248,42,21,100,160,15,21,35,208,7,2,10,208,7,11,35,120,0,17,1,208,7,2,6,228,12,0,116,120,0,17,1,208,7,2,8,220,5,4,30,60,0,11,1,208,7,2,6,220,5,4,30,40,0,11,1,208,7,2,7,232,3,21,20,60,0,11,1,208,7,2,10,32,3,11,8,60,0,11,1,208,7,2,5,136,19,4,130,220,5,4,40,208,7,2,6,64,31,11,150,220,5,11,40,208,7,2,15,16,39,0,90,208,7,0,18,208,7,2,15,184,11,0,27,232,3,0,10,208,7,2,15,152,58,0,125,136,19,21,45,208,7,2,3,220,5,11,70,136,19,11,215,208,7,2,5,232,3,4,26,184,11,4,85,208,7,2,10,208,7,11,35,120,0,17,1,208,7,2,10,220,5,4,15,60,0,11,1,208,7,2,10,208,7,21,18,60,0,4,1,208,7,2,8,220,5,11,15,40,0,11,1,208,7,2,14,184,11,0,25,120,0,17,1,208,7,2,8,16,39,4,170,184,11,4,56,208,7,2,14,224,46,21,112,160,15,21,36,208,7,2,10,208,7,11,35,120,0,17,1,208,7,2,6,184,11,0,100,120,0,17,1,208,7,2,8,220,5,4,30,60,0,11,1,208,7,2,6,176,4,0,24,40,0,11,1,208,7,2,7,232,3,21,20,60,0,11,1,208,7,2,10,32,3,11,8,60,0,11,1,208,7,2,5,136,19,4,100,220,5,4,45,208,7,2,6,64,31,11,136,220,5,11,36,208,7,2,15,16,39,0,90,208,7,0,16,208,7,2,15,184,11,0,28,232,3,0,10,208,7,2,6,64,31,11,136,220,5,11,36,208,7,2,15,16,39,0,90,208,7,0,16,208,7,2,93,184,11,0,28,232,3,0,10,8,2,20,43,10,0,0,0,0,0,0,0)
    /**
     * 读取类
     * */
    val gattRead:CWGattRead by lazy {
        return@lazy CWGattRead(this)
    }
    /**
     * 写入类
     * */
    private val gattWrite:CWGattWrite by lazy {
        return@lazy CWGattWrite(this)
    }

    fun removeSelf(){
        val isRemove = CWBleManager.mCWDevices.remove(this)
        Log.d(tag,"删除外接设备成功与否:$isRemove")
    }



    /**********************  CWGattReadInterface  start  **************************/
    override fun cwBleBatteryPower(value: Int) {

    }

    override fun cwBleRecipeLoadingCallback(flag: Boolean, duration: Int) {
        if (flag){
            mDuration = duration
            gattWrite.cwBleWriteOutputModel(2)
        }
    }

    override fun cwBleRecipeSendIndexCallback(index: Int, total: Int) {
        val max = mRecipeContent.count() / 12 - 1
        if (max == total) {
            Log.d(tag,"加载处方~~~~")
            gattWrite.cwBleWriteLoadingRecipe()
        }
    }

    override fun cwBleSoftwareStartCureCallback(flag: Boolean) {
        Log.d(tag,"开始输出:$flag")
    }

    override fun cwBleSoftwareStopCureCallback(flag: Boolean) {
        Log.d(tag,"停止输出:$flag")
    }

    /**
     * 渠道验证:错误
     * */
    override fun cwChannelCheckError(){

    }

    /**
     * 渠道验证:成功
     * randoms:设备端传过来的随机数
     * */
    override fun cwChannelCheckSuccess(randoms:List<Int>){
        gattWrite.cwBleWriteACK(randoms)
    }

    /**
     * 选择设备
     * - Parameter flag: 成功与否
     * */
    override fun cwBleSelectDeviceCallback(flag: Boolean){

    }

    /**
     * 软件端发送强度指令 回调
     * - Parameter flag: 成功与否
     * */
    override fun cwBleSetIntensityCallback(flag: Boolean){

    }

    /**
     * 软件端发送输出模式 回调
     * - Parameter flag: 成功与否
     * */
    override fun cwBleSetOutputSchemeCallback(flag: Boolean){
        //todo:到此处 可以开始输出了～
    }

    /**
     * 软件端主动查询电极状态
     * - extensionIsInsert: 扩展电极是否插入
     * - main: 主电极贴合状态
     * - extension1: 扩展电极1状态
     * - extension2: 扩展电极2状态
     * */
    override fun cwrBleElectrodeQueryCallback(extensionIsInsert:Boolean,main:Int,extension1:Int,extension2:Int){

    }

    /**
     * 硬件主动发送的电极贴合信息
     * - isClose: 设备是否关机
     * - extensionIsInsert: 扩展电极是否插入
     * - main: 主电极贴合状态
     * - extension1: 扩展电极1状态
     * - extension2: 扩展电极2状态
     * */
    override fun cwBleElectrodeNotify(isClose:Boolean,extensionIsInsert:Boolean,main:Int,extension1:Int,extension2:Int){

    }

    /**
     * 硬件主动 扩展电极状态通知
     * - flag: false 拔下 true插入
     * */
    override fun cwBleExtensionElectrodeInsertNotify(flag: Boolean){

    }

    /**
     * 硬件主动发送的处方播放完成通知
     * */
    override fun cwBlePlayCompleteNotify(flag: Boolean){

    }

    /**
     * 强度通知 硬件主动发送
     * - value: 强度
     * */
    override fun cwBleIntensityNotify(value: Int){

    }

    /**
     * 理疗开始or暂停 通知
     * Parameter flag: false 暂停 true 开始
     * */
    override fun cwBleCureStatusNotify(flag: Boolean){

    }

    /**
     * 软件端锁定设备按钮
     * */
    override fun cwBleLockDeviceButtonCallback(flag: Boolean){

    }

    /**
     * 硬件按钮解锁设备通知
     * */
    override fun cwBleUnlockDeviceButtonNotify(flag: Boolean){

    }

    /***
     * 软件查询设备状态回调
     * - isPlay: 播放状态
     * - intensity: 强度
     * - recipeId: 处方id
     * - playTime: 已播放时长
     * */
    override fun cwBleDeviceStatusQueryCallback(isPlay:Boolean,intensity:Int,recipeId:Int,playTime:Int){

    }

    /**
     * 设备解锁 -验证阶段的解锁
     * flag: 成功与否
     * */
    override fun cwBleDeviceUnlockCallback(flag: Boolean){
        if (flag) {
            gattWrite.cwBleWriteQueryRangeMap()
        }
    }

    /**
     * 幅度表
     * - version: 版本
     * - steps: 步进数
     * */
    override fun cwBleDeviceRangeMapCallback(version:Int,steps:Int){

        val localVer = CWBleManager.configure.channel.rangeMap[0]
        when(version){
            0,1,2 -> {
                Log.d(tag,"直接写入处方内容")
                gattWrite.cwBleWriteRecipeContent(1,mRecipeContent)
            }
            else -> {
                if (localVer > version){
                    Log.d(tag,"去更新步进表")
                    //TODO:更新步进表
                }else{
                    Log.d(tag,"去写入处方内容")
                    gattWrite.cwBleWriteRecipeContent(1,mRecipeContent)
                }
            }
        }

    }

    /**
     * 幅度表 写入回调
     * - index: 第几条
     * - flag: 成功与否
     * */
    override fun cwBleDeviceRangeMapWriteCallback(index:Int,flag:Boolean){

    }

    /**
     * 更新设备幅度表 回调
     * - flag: 是否成功
     * */
    override fun cwBleDeviceRangeMapUpdateCallback(flag: Boolean){

    }
    /**********************  CWGattReadInterface  end  **************************/


    /**********************  CWGattWriteInterface start  ************************/
    override fun cwGattWriteData(list: List<Int>) {
        val service = gatt.getService(CWGattAttributes.CW_SERVICE_UUID)
        val char = service.getCharacteristic(CWGattAttributes.CW_CHARACTER_writeUUID)
        val bytes = list.map { it.toByte() }
        char.value = bytes.toByteArray()
        gatt.writeCharacteristic(char)
    }
    /**********************  CWGattWriteInterface end  ************************/


    /**
     * 开始往设备写入数据
     * */
    fun writeData(){
        gattWrite.cwBleWriteChannelCode(CWBleManager.configure.channel.encrypt_Channel_Code)
    }
    /**
     * 此方法为了让写入处方内容防止出现mDeviceBusy=true
     * 不是办法的办法
     * */
    fun readCharacteristicData(list: ByteArray){
        val maps = list.map { it.toInt() }
        if (maps.count() > 3){
            val command = maps[0] and 0xff
            val s = maps[1] and 0xff
            when(command){
                0xab -> {//写入处方
                    when(s) {
                        0x00 -> {
                            val index = maps[2]
                            if (index == 0) {
                                return
                            }
                            gattWrite.cwBleWriteRecipeContent(index + 1,mRecipeContent)
                        }
                        else -> {

                        }
                    }
                }
                else -> {

                }
            }
        }
    }
}


