package com.example.chitwing.anycure_kotlin_master.ble

import java.time.Duration

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/11
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
interface CWGattReadInterface {

    /**
     * 电池电量
     * */
    fun cwBleBatteryPower(value:Int)
    /**
    * 发送处方回调
    * index:第几帧
    * total:已发送帧数
     * app向蓝牙设备发送处方 12/帧
    * */
    fun cwBleRecipeSendIndexCallback(index:Int,total:Int)
    /**
     * 软件端发送 处方加载 指令 回调
     * flag:处方是否有效
     * duration:处方的时间
     * */
    fun cwBleRecipeLoadingCallback(flag:Boolean,duration: Int)

    /**
     * 软件端发送指令 开始输出 硬件返回
     * */
    fun cwBleSoftwareStartCureCallback(flag: Boolean)

    /**
     *软件端发送指令 暂停输出 硬件返回
     * */
    fun cwBleSoftwareStopCureCallback(flag: Boolean)

    /**
     * 渠道验证:错误
     * */
    fun cwChannelCheckError()

    /**
     * 渠道验证:成功
     * randoms:设备端传过来的随机数
     * */
    fun cwChannelCheckSuccess(randoms:List<Int>)

    /**
     * 选择设备
     * - Parameter flag: 成功与否
     * */
    fun cwBleSelectDeviceCallback(flag: Boolean)

    /**
     * 软件端发送强度指令 回调
     * - Parameter flag: 成功与否
     * */
    fun cwBleSetIntensityCallback(flag: Boolean)

    /**
     * 软件端发送输出模式 回调
     * - Parameter flag: 成功与否
     * */
    fun cwBleSetOutputSchemeCallback(flag: Boolean)

    /**
     * 软件端主动查询电极状态
     * - extensionIsInsert: 扩展电极是否插入
     * - main: 主电极贴合状态
     * - extension1: 扩展电极1状态
     * - extension2: 扩展电极2状态
     * */
    fun cwBleElectrodeQueryCallback(extensionIsInsert:Boolean,main:Int,extension1:Int,extension2:Int)

    /**
     * 硬件主动发送的电极贴合信息
     * - isClose: 设备是否关机
     * - extensionIsInsert: 扩展电极是否插入
     * - main: 主电极贴合状态
     * - extension1: 扩展电极1状态
     * - extension2: 扩展电极2状态
     * */
    fun cwBleElectrodeNotify(isClose:Boolean,extensionIsInsert:Boolean,main:Int,extension1:Int,extension2:Int)

    /**
     * 硬件主动 扩展电极状态通知
     * - flag: false 拔下 true插入
     * */
    fun cwBleExtensionElectrodeInsertNotify(flag: Boolean)

    /**
     * 硬件主动发送的处方播放完成通知
     * */
    fun cwBlePlayCompleteNotify(flag: Boolean)

    /**
     * 强度通知 硬件主动发送
     * - value: 强度
     * */
    fun cwBleIntensityNotify(value: Int)

    /**
     * 理疗开始or暂停 通知
     * Parameter flag: false 暂停 true 开始
     * */
    fun cwBleCureStatusNotify(flag: Boolean)

    /**
     * 软件端锁定设备按钮
     * */
    fun cwBleLockDeviceButtonCallback(flag: Boolean)

    /**
     * 硬件按钮解锁设备通知
     * */
    fun cwBleUnlockDeviceButtonNotify(flag: Boolean)

    /***
     * 软件查询设备状态回调
     * - isPlay: 播放状态
     * - intensity: 强度
     * - recipeId: 处方id
     * - playTime: 已播放时长
     * */
    fun cwBleDeviceStatusQueryCallback(isPlay:Boolean,intensity:Int,recipeId:Int,playTime:Int)

    /**
     * 设备解锁 -验证阶段的解锁
     * flag: 成功与否
     * */
    fun cwBleDeviceUnlockCallback(flag: Boolean)

    /**
     * 幅度表
     * - version: 版本
     * - steps: 步进数
     * */
    fun cwBleDeviceRangeMapCallback(version:Int,steps:Int)

    /**
     * 幅度表 写入回调
     * - index: 第几条
     * - flag: 成功与否
     * */
    fun cwBleDeviceRangeMapWriteCallback(index:Int,flag:Boolean)

    /**
     * 更新设备幅度表 回调
     * - flag: 是否成功
     * */
    fun cwBleDeviceRangeMapUpdateCallback(flag: Boolean)


}