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

    /*


    /// 软件端发送指令 硬件返回 暂停输出
    func jrBleSoftwareStopCureCallback(_ flag:Int8, per: CBPeripheral, chars: CBCharacteristic);

    /// 选择设备
    ///
    /// - Parameter flag: 成功与否
    func jrBleSelectDeviceCallback(_ flag:Bool,per: CBPeripheral, chars: CBCharacteristic);

    /// 软件端发送强度指令 回调
    ///
    /// - Parameter flag: 成功与否
    func jrBleSetIntensityCallback(_ flag:Bool,per: CBPeripheral, chars: CBCharacteristic);

    /// 软件端发送输出模式 回调
    ///
    /// - Parameter flag: 成功与否
    func jrBleSetOutputSchemeCallback(_ flag:Bool,per: CBPeripheral, chars: CBCharacteristic);

    /// 软件端主动查询电极状态
    ///
    /// - Parameters:
    ///   - extensionIsInsert: 扩展电极是否插入
    ///   - main: 主电极贴合状态
    ///   - extension1: 扩展电极1状态
    ///   - extension2: 扩展电极2状态
    func jrBleElectrodeQueryCallback(_ extensionIsInsert:Bool,main:Int8,extension1:Int8,extension2:Int8,per: CBPeripheral, chars: CBCharacteristic);

    /// 硬件主动发送的电极贴合信息
    ///
    /// - Parameters:
    ///   - extensionIsInsert: 扩展电极是否插入
    ///   - main: 主电极贴合状态
    ///   - isClose: 设备是否关机
    ///   - extension1: 扩展电极1状态
    ///   - extension2: 扩展电极2状态
    func jrBleElectrodeNotify(_ extensionIsInsert:Bool,main:Int8,isClose:Bool,extension1:Int8,extension2:Int8,per: CBPeripheral, chars: CBCharacteristic);

    /// 硬件主动 扩展电极状态通知
    ///
    /// - Parameters:
    ///   - flag: false 拔下 true插入
    func jrBleExtensionElectrodeInsertNotify(_ flag:Bool,per: CBPeripheral, chars: CBCharacteristic);

    /// 硬件主动发送的处方播放完成通知
    func jrBlePlayCompleteNotify(_ flag:Bool);

    /// 软件端电量查询
    ///
    /// - Parameters:
    ///   - power: 电量
    func jrBleBatteryPowerQueryCallback(_ power:Int8,per: CBPeripheral, chars: CBCharacteristic);

    /// 强度通知 硬件主动发送
    ///
    /// - Parameters:
    ///   - intensity: 强度
    func jrBleIntensityNotify(_ intensity:Int8,per: CBPeripheral, chars: CBCharacteristic);

    /// 理疗开始or暂停 通知
    ///
    /// - Parameter flag: false 暂停 true 开始
    func jrBleCureStatusNotify(_ flag:Bool,per: CBPeripheral, chars: CBCharacteristic);

    /// 软件端锁定设备按钮
    ///
    /// - Parameter flag: --
    func jrBleLockDeviceButtonCallback(_ flag:Bool,per: CBPeripheral, chars: CBCharacteristic);

    /// 硬件按钮解锁设备通知
    ///
    /// - Parameter flag: --
    func jrBleUnlockDeviceButtonNotify(_ flag:Bool,per: CBPeripheral, chars: CBCharacteristic);

    /// 软件查询设备状态回调
    ///
    /// - Parameters:
    ///   - isPlay: 播放状态
    ///   - intensity: 强度
    ///   - recipeId: 处方id
    ///   - playTime: 已播放时长
    func jrBleDeviceStatusQueryCallback(_ isPlay:Bool,intensity:Int8,recipeId:Int16,playTime:Int,per: CBPeripheral, chars: CBCharacteristic);

    /// 渠道验证的时候 硬件返回的随机数 正确的渠道号
    ///
    /// - Parameter randoms: 随机数组
    func jrBleChannelCheckCallback(_ randoms:Array<Int>,per: CBPeripheral, chars: CBCharacteristic);

    /// 渠道验证的时候 错误的渠道号
    ///
    func jrBleChannelCheckErrorCallback(_ per: CBPeripheral, chars: CBCharacteristic);

    /// 设备解锁--验证阶段的解锁
    ///
    /// - Parameter flag: 成功与否
    func jrBleDeviceUnlockCallback(_ flag:Bool,per: CBPeripheral, chars: CBCharacteristic);

    /// 幅度表
    ///
    /// - Parameters:
    ///   - version: 版本
    ///   - steps: 步进数
    func jrBleDeviceRangeMapCallback(_ version:Int8,steps:Int8,per: CBPeripheral, chars: CBCharacteristic);

    /// 幅度表
    ///
    /// - Parameters:
    ///   - index: 第几条
    ///   - flag: 成功与否
    func jrBleDeviceRangeMap(_ index:Int8,flag:Bool,per: CBPeripheral, chars: CBCharacteristic);

    /// 设备更新幅度表 是否成功
    ///
    /// - Parameters:
    ///   - flag: 是否成功
    func jrBleDeviceRangeMapUpdate(_ flag:Bool,per: CBPeripheral, chars: CBCharacteristic);
    * */

}