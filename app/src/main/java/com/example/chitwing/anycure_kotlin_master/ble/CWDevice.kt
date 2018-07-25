package com.example.chitwing.anycure_kotlin_master.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.model.Recipe

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
data class CWDevice ( val mDevice:BluetoothDevice, var mGatt:BluetoothGatt?):CWGattReadInterface,CWGattWriteInterface{


    private val tag = "CWDevice"

    /**
     * 处方
     * */
    var recipe:Recipe? = null

    /**
     * 处方的时间
     * */
    var mDuration:Int = 1200
    /**
     * 处方已播放的时间
     * */
    var playDuration:Int = 0
    /**
     * 回调
     * */
    var mCallback:CWDeviceInterface? = null
    /**
     * 处方是否在播放
     * */
    var isPlay:Boolean = false
    /**
     * 强度
     * */
    var intensity:Int = 0
    /**
     * 电池电量
     * */
    var power:Int = 0


    /**
     * 处方内容
     * */
    private val mRecipeContent by lazy {
        return@lazy recipe?.getRecipeContent()
    }

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
    /**
     * 记录是否当前的外设
     * */
    var isSelect:Boolean = false

    /**
     * 记录是否主动断开
     * 默认为否 当非主动断开时候 需要重连
     * */
    var isAutoDisconnect:Boolean = true
    /**
     * 退出
     * */
    private var isExit:Boolean = false
    /**
     * 设备的连接状态
     * */
    var isConnect:Boolean = false

    fun removeSelf(){
        isAutoDisconnect = true
        mGatt?.disconnect()
        mGatt?.close()
        mGatt = null
        mCallback = null
        val isRemove = CWBleManager.mCWDevices.remove(this)
        Log.d(tag,"删除外接设备成功与否:$isRemove")
    }


    /**********************  CWGattReadInterface  start  **************************/
    override fun cwBleBatteryPower(value: Int) {
        this.power = value
        mCallback?.transferPower(value,this)
    }

    override fun cwBleRecipeLoadingCallback(flag: Boolean, duration: Int) {
        if (isExit) {
            endCureNotify()
            return
        }
        if (flag){
            mDuration = duration
            gattWrite.cwBleWriteOutputModel(2)
        }
    }

    override fun cwBleRecipeSendIndexCallback(index: Int, total: Int) {
        mRecipeContent?.let {
            val max = it.count() / 12 - 1
            if (max == total) {
                gattWrite.cwBleWriteLoadingRecipe()
            }
        }
    }

    override fun cwBleSoftwareStartCureCallback(flag: Boolean) {
        this.isPlay = true
        mCallback?.cureStartEvent(this)
    }

    override fun cwBleSoftwareStopCureCallback(flag: Boolean) {
        if (isExit) {
            gattWrite.cwBleWriteLoadingRecipe()
            return
        }
        this.isPlay = false
        mCallback?.cureStopEvent(this)
    }

    /**
     * 渠道验证:错误
     * */
    override fun cwChannelCheckError(){
        mCallback?.prepareFail("渠道验证错误",this)
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
        mCallback?.prepareComplete(this)
        isAutoDisconnect = false
    }

    /**
     * 软件端主动查询电极状态
     * - extensionIsInsert: 扩展电极是否插入
     * - main: 主电极贴合状态
     * - extension1: 扩展电极1状态
     * - extension2: 扩展电极2状态
     * */
    override fun cwBleElectrodeQueryCallback(extensionIsInsert:Boolean,main:Int,extension1:Int,extension2:Int){
        mCallback?.transferMainElectrodeQuery(main,this)
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
        if (isClose){
            this.isPlay = false
            mCallback?.deviceCloseEvent(this)
            removeSelf()
            return
        }
        mCallback?.transferMainElectrodeNotify(main,this)

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
        isPlay = false
        mCallback?.cureEndEvent(this)
    }

    /**
     * 强度通知 硬件主动发送
     * - value: 强度
     * */
    override fun cwBleIntensityNotify(value: Int){
        this.intensity = value
        mCallback?.transferIntensity(value,this)
    }

    /**
     * 理疗开始or暂停 通知
     * Parameter flag: false 暂停 true 开始
     * */
    override fun cwBleCureStatusNotify(flag: Boolean){
        isPlay = flag
        if (flag){
            mCallback?.cureStartEvent(this)
        }else{
            mCallback?.cureStopEvent(this)
        }
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
        this.isPlay = isPlay
        this.playDuration = this.mDuration - playTime
        this.intensity = intensity
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
            in 0 .. 2 -> {
                Log.d(tag,"直接写入处方内容")
                gattWrite.cwBleWriteRecipeContent(1,mRecipeContent)
            }
            else -> {
                if (localVer > version){
                    Log.d(tag,"去更新步进表 ->写入步进表内容")
                    gattWrite.cwBleWriteRangeMapContent(0)
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
        Log.d(tag,"写入的幅度表->$flag 帧数->$index")
        if (flag){
            val max = CWBleManager.configure.channel.rangeMap.count() / 16
            if ((index + 1) == max){
                gattWrite.cwBleWriteToUpdateRangeMap()
            }
        }else{
            mCallback?.prepareFail("幅度表写入失败 帧数:$index",this)
        }
    }

    /**
     * 更新设备幅度表 回调
     * - flag: 是否成功
     * */
    override fun cwBleDeviceRangeMapUpdateCallback(flag: Boolean){
        if (flag){
            gattWrite.cwBleWriteRecipeContent(1,mRecipeContent)
        }else{
            mCallback?.prepareFail("更新幅度表失败",this)
        }
    }
    /**********************  CWGattReadInterface  end  **************************/


    /**********************  CWGattWriteInterface start  ************************/
    override fun cwGattWriteData(list: List<Int>) {
        val service = mGatt?.getService(CWGattAttributes.CW_SERVICE_UUID)
        val char = service?.getCharacteristic(CWGattAttributes.CW_CHARACTER_writeUUID)
        val bytes = list.map { it.toByte() }
        char?.value = bytes.toByteArray()
        mGatt?.writeCharacteristic(char)
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
     * 从
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
                0xad -> {//写入幅度映射表
                    //TODO:可能需要～做幅度映射表矫正
                }
                else -> {

                }
            }
        }
    }

    /**~~~~~~~~~~~~~~~ 操作功能 ~~~~~~~~~~~~~~~~~**/
    /**
     * 结束理疗
     * */

    private fun endCureNotify(){
        mCallback?.cureEndEvent(this)
        removeSelf()
    }

    /**
     * 结束
     * */
    fun endCureAction(){
        isExit = true
        if (isConnect){
            stopCureAction()
        }else{
            endCureNotify()
        }
    }
    /**
     * 开始
     * */
    fun startCureAction(){
        gattWrite.cwBleWriteStartCure()
    }
    /**
     * 暂停
     * */
    fun stopCureAction(){
        gattWrite.cwBleWriteStopCure()
    }
    /**
     * 强度增加
     * */
    fun addIntensity(){
        intensity += 1
        if (intensity > 50){
            intensity = 50
        }
        setBleIntensity()
    }
    /**
     * 强度减少
     * */
    fun minusIntensity(){
        intensity -= 1
        if (intensity < 0 ){
            intensity = 0
        }
        setBleIntensity()
    }

    /**
     * 选择设备
     * arg-> false 为取消选择 true为选择
     * */
    fun selectDevice(arg:Boolean){
        val value = if (arg) 1 else 0
        gattWrite.cwBleWriteSelectDevice(value)
    }
    /**
     * 写入强度
     * */
    private fun setBleIntensity(){
        gattWrite.cwBleWriteIntensity(intensity)
        mCallback?.transferIntensity(intensity,this)
    }


}


