package com.example.chitwing.anycure_kotlin_master.ble

import android.util.Log
import com.orhanobut.logger.Logger
import kotlin.experimental.and

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/11
 * QQ/Tel/Mail:383118832
 * Description:蓝牙数据读取
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWGattRead(b:CWGattReadInterface) :CWGattReadInterface by b{
    private val tag = "CWGattRead"

    fun handleData(list:ByteArray?){

        val first = list?.first()?.toInt()
        first?.let {
            when(it and 0xff){
                /**
                 * 设备发过来到信息(除去0xab)
                 * */
                0x55 -> {
                    deviceData(list)
                }
                /**
                 * 发送处方返回
                 * */
                0xab -> {
                    recipeContextSendComplete(list)
                }
                /**
                 *渠道验证
                 * */
                0xce -> {
                    handleChannelData(list)
                }
                /**
                 * 幅度映射表
                 * */
                0xad -> {
                    handleRangeMapData(list)
                }
                /**
                 * 默认
                 * */
                else -> {
                    Logger.d("数据读取:默认$it")
                }
            }
        }
    }

    /**
     * 处方回调
     * */
    private fun recipeContextSendComplete(byteArray: ByteArray){
        if (byteArray.count() > 3){
            val index = byteArray[2].toInt()
            val total = byteArray[3].toInt()
            Logger.d("处方第几帧:$index 总共帧数:$total")
            cwBleRecipeSendIndexCallback(index,total)
        }
    }

    /**
     * 设备数据主要交互
     * */
    private fun deviceData(list: ByteArray){
        /**异常处理*/
        if (list.count() <= 1){
            assert(true,{
                Logger.d("可能发生数据越界")
            })
            return
        }
        val command = list[1].toInt()
        val desc:String?
        when(command){
            0x00 -> {
                desc = "停止输出"
                softwareStopOutputCallback(list)
            }
            0x01 ->{
                desc = "开始输出"
                softwareStartOutputCallback(list)
            }
            0x02 -> {
                desc = "选择设备"
                softwareSelectDevice(list)
            }
            0x03 -> {
                desc = "调节强度"
                softwareSetIntensity(list)
            }
            0x04 -> {
                desc = "输出模式"
                deviceDataOutputModel(list)
            }
            0x05 -> {
                desc = "未知指令:05"
            }
            0x06 -> {
                desc = "处方加载"
                deviceDataRecipeLoading(list)
            }
            0x07 -> {
                desc = "电极贴贴合状态查询"
                softwareElectrodeQuery(list)
            }
            0x08 -> {
                desc = "电极贴贴合状态 设备主动通知"
                hardwareElectrodeNotify(list)
            }
            0x09 -> {
                desc = "未知指令:09"
            }
            0x0a -> {
                desc = "播放完成通知 设备主动通知"
                hardwarePlayComplete(list)
            }
            0x0b -> {
                desc = "电池电量查询 设备主动通知"
                batteryPower(list)
            }
            0x0d -> {
                desc = "扩展电极状态 设备主动通知"
                hardwareExtensionElectrodeNotify(list)
            }
            0x10 -> {
                desc = "电极强度 设备主动通知"
                hardwareIntensityNotify(list)
            }
            0x11 -> {
                desc = "输出开始or暂停 设备主动通知"
                hardwareOutputNotify(list)
            }
            0x12 -> {
                desc = "设备状态信息获取"
                softwareQueryDeviceInfo(list)
            }
            0x16 -> {
                desc = "设备按钮锁定or解除"
                softwareLockDeviceButton(list)
            }
            0x17 -> {
                desc = "手动解除按钮锁 设备主动通知"
                hardwareUnlockDeviceButton(list)
            }
            else -> {
                desc =  "默认指令:未知->" + "$command"
            }
        }
        Logger.d(desc)
    }

    /**
     * 设备电池电量
     * */
    private fun batteryPower(list: ByteArray){
        if (list.count() > 2){
            val value = list[2].toInt()
            cwBleBatteryPower(value)
            Logger.d("电池电量:$value")
        }
    }

    /**
     * 渠道相关数据
     * */
    private fun handleChannelData(list: ByteArray){
        if (list.count() > 1){
            val command = list[1].toInt() and 0xff
            when(command){
                0x11 -> {
                    checkChannel(list)
                }
                0x12 -> {
                    unlockDevice(list)
                }
                else -> {
                    Logger.d("渠道验证:未知$command")
                }
            }
        }
    }

    /**
     * 渠道验证
     * */
    private fun checkChannel(list: ByteArray){
        val subs = list.slice(2 .. list.lastIndex)
        val maps = subs.map { it.toInt() }
        val result = maps.reduce { total, next -> total + next }
        if (result == 0){//渠道错误
            cwChannelCheckError()
            Logger.d("渠道验证错误")
        }else{
            cwChannelCheckSuccess(maps)
            Logger.d("渠道验证成功")
        }
    }

    /**
     * 设备解锁
     * */
    private fun unlockDevice(list: ByteArray){
        val flag = list[2].toInt() == 0
        cwBleDeviceUnlockCallback(flag)
        Logger.d("设备解锁:$flag")
    }


    /**
     * 幅度映射表
     * - list: 蓝牙设备传输过来的数据
     * */
    private fun handleRangeMapData(list: ByteArray){
        val command = list[1].toInt() and 0xff
        when(command){
            0x01 -> {
                rangeMapVersion(list)
            }
            0x02 -> {
                rangeMapWriteContent(list)
            }
            0x03 -> {
                rangeMapUpdate(list)
            }
            else -> {

            }
        }
    }

    ///映射表的版本号
    private fun rangeMapVersion(list: ByteArray){
        if (list.count() > 3){
            val ver = list[2].toInt() and 0xff
            val steps = list[3].toInt() and 0xff
            cwBleDeviceRangeMapCallback(ver,steps)
            Logger.d("步进表版本ver:$ver 步进数:$steps")
        }
    }

    ///映射表的内容
    private fun rangeMapWriteContent(list: ByteArray){
        if (list.count() > 3){
            val index = list[2].toInt()
            //0 成功 1 失败
            val status = list[3].toInt() == 0
            cwBleDeviceRangeMapWriteCallback(index,status)
            Logger.d("写入步进表 帧数$index 状态$status")
        }
    }
    ///映射表升级
    private fun rangeMapUpdate(list: ByteArray){
        if (list.count() > 2){
            val status = list[2].toInt() == 0
            cwBleDeviceRangeMapUpdateCallback(status)
            Logger.d("更新步进表$status")
        }
    }

    /**
     * 处方加载
     * */
    private fun deviceDataRecipeLoading(list: ByteArray){
        val flag = list[4].toInt() == 1
        val low = list[2].toInt() and 0xff
        val high = list[3].toInt() * 256
        val dur = low + high
        Logger.d("处方是否有效:$flag ,处方时长:$dur")
        cwBleRecipeLoadingCallback(flag,dur)
    }

    /**
     * 输出模式
     * */
    private fun deviceDataOutputModel(list: ByteArray){
        Logger.d("设置输出模式:成功")
        cwBleSetOutputSchemeCallback(true)
    }

    /**
    * 停止输出
    * */
    private fun softwareStopOutputCallback(list: ByteArray){
        cwBleSoftwareStopCureCallback(true)
    }
    /**
     * 开始输出
     * */
    private fun softwareStartOutputCallback(list: ByteArray){
        cwBleSoftwareStartCureCallback(true)
    }
    /**
     * 选择设备
     * */
    private fun softwareSelectDevice(list: ByteArray){
        cwBleSelectDeviceCallback(true)
    }
    /**
     * 调节强度
     * */
    private fun softwareSetIntensity(list: ByteArray){
        cwBleSetIntensityCallback(true,1)
    }
    /**
     * 电极贴贴合 软件状态查询
     * */
    private fun softwareElectrodeQuery(list: ByteArray){
        if (list.count() > 5){
            val isInsert = list[2].toInt() == 1//0未插入 1插入
            val main = list[3].toInt()//电极贴贴合
            val e1 = list[4].toInt()
            val e2 = list[5].toInt()
            Logger.d("软件 电极贴贴合状态查询 贴合:$main")
            cwBleElectrodeQueryCallback(isInsert,main,e1,e2)
        }
    }
    /**
     * 电极贴贴合状态 设备主动通知
     * */
    private fun hardwareElectrodeNotify(list: ByteArray){
        if (list.count() > 6){
            val isInsert = list[2].toInt() == 1//0未插入 1插入
            val main = list[3].toInt()//电极贴贴合
            val e1 = list[4].toInt()
            val e2 = list[5].toInt()
            val isClose = list[6].toInt() == 1//0正常工作 1即将关机
            Logger.d("硬件 电极贴贴合状态通知 关机位:$isClose 贴合位:$main")
            cwBleElectrodeNotify(isClose,isInsert,main,e1,e2)
        }
    }
    /**
     * 播放完成通知 设备主动通知
     * */
    private fun hardwarePlayComplete(list: ByteArray){
        if (list.count() > 2){
            val flag = list[2].toInt() == 1
            Logger.d("播放完成通知:$flag")
            cwBlePlayCompleteNotify(flag)
        }
    }
    /**
     * 扩展电极状态 设备主动通知
     * */
    private fun hardwareExtensionElectrodeNotify(list: ByteArray){
        if (list.count() > 2){
            val flag = list[2].toInt() == 1
            Logger.d("扩展电极状态通知:$flag")
            cwBleExtensionElectrodeInsertNotify(flag)
        }
    }
    /**
     * 电极强度 设备主动通知
     * */
    private fun hardwareIntensityNotify(list: ByteArray){
        if (list.count() > 2){
            val value = list[2].toInt()
            Logger.d("电极强度通知:$value")
            cwBleIntensityNotify(value)
        }
    }
    /**
     * 输出开始 or 暂停 设备主动通知
     * */
    private fun hardwareOutputNotify(list: ByteArray) {
        if (list.count() > 2){
            val flag = list[2].toInt() == 1//0暂停 1开始
            Logger.d("硬件 输出开始or暂停通知$flag")
            cwBleCureStatusNotify(flag)
        }
    }
    /**
     * 设备状态信息获取
     * */
    private fun softwareQueryDeviceInfo(list: ByteArray){
        if (list.count() <= 6){
            return
        }
        val isPlay = list[2].toInt() == 1//0 暂停 1 播放
        val intensity = list[3].toInt()
        val recipeId = list[4].toInt()
        val dur = list[5].toInt() and 0xff * 256 + list[6].toInt() and 0xff
        Logger.d("设备信息 播放状态:$isPlay -- 强度:$intensity -- 处方id：$recipeId -- 播放时间:$dur")
        cwBleDeviceStatusQueryCallback(isPlay,intensity,recipeId,dur)
    }
    /**
     * 设备按钮锁定 or 解除
     * */
    private fun softwareLockDeviceButton(list: ByteArray){
        if (list.count() > 2) {
            val flag = list[2].toInt() == 1
            Logger.d("设备按钮锁定 or 解除:$flag")
            cwBleLockDeviceButtonCallback(flag)
        }
    }

    /**
     * 手动解除按钮锁 设备主动通知
     * */
    private fun hardwareUnlockDeviceButton(list: ByteArray){
        if (list.count() > 2){
            val flag = list[2].toInt() == 1
            Logger.d("手动解除按钮锁 设备主动通知:$flag")
            cwBleUnlockDeviceButtonNotify(flag)
        }
    }



}