package com.example.chitwing.anycure_kotlin_master.ble

import android.util.Log

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
                    Log.d(tag,"数据读取:默认$it")
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
            Log.d(tag,"处方第几帧:$index 总共帧数:$total")
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
                Log.d(tag,"可能发生数据越界")
            })
            return
        }
        val command = list[1].toInt()
        val desc:String?
        when(command){
            0x00 -> {
                desc = "停止输出"
            }
            0x01 ->{
                desc = "开始输出"
            }
            0x02 -> {
                desc = "选择设备"
            }
            0x03 -> {
                desc = "调节强度"
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
            }
            0x08 -> {
                desc = "电极贴贴合状态 设备主动通知"
            }
            0x09 -> {
                desc = "未知指令:09"
            }
            0x0a -> {
                desc = "播放完成通知 设备主动通知"
            }
            0x0b -> {
                desc = "电池电量查询 设备主动通知"
                batteryPower(list)
            }
            0x0d -> {
                desc = "扩展电极状态 设备主动通知"
            }
            0x10 -> {
                desc = "电极强度 设备主动通知"
            }
            0x11 -> {
                desc = "输出开始or暂停 设备主动通知"
            }
            0x12 -> {
                desc = "设备状态信息获取"
            }
            0x16 -> {
                desc = "设备按钮锁定or解除"
            }
            0x17 -> {
                desc = "手动解除按钮锁 设备主动通知"
            }
            else -> {
                desc =  "默认指令:未知->" + "$command"
            }
        }
        Log.d(tag,desc)
    }

    /**
     * 设备电池电量
     * */
    private fun batteryPower(list: ByteArray){
        if (list.count() > 2){
            val value = list[2].toInt()
            cwBleBatteryPower(value)
            Log.d(tag,"电池电量:$value")
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
                    Log.d(tag,"渠道验证:未知$command")
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
            Log.d(tag,"渠道验证错误")
        }else{
            cwChannelCheckSuccess(maps)
            Log.d(tag,"渠道验证成功")
        }
    }

    /**
     * 设备解锁
     * */
    private fun unlockDevice(list: ByteArray){
        val flag = list[2].toInt() == 0
        cwBleDeviceUnlockCallback(flag)
        Log.d(tag,"设备解锁:$flag")
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
            Log.d(tag,"步进表版本ver:$ver 步进数:$steps")
        }
    }

    ///映射表的内容
    private fun rangeMapWriteContent(list: ByteArray){
        if (list.count() > 3){
            val index = list[2].toInt()
            //0 成功 1 失败
            val status = list[3].toInt() == 0
            cwBleDeviceRangeMapWriteCallback(index,status)
            Log.d(tag,"写入步进表 帧数$index 状态$status")
        }
    }
    ///映射表升级
    private fun rangeMapUpdate(list: ByteArray){
        if (list.count() > 2){
            val status = list[2].toInt() == 0
            cwBleDeviceRangeMapUpdateCallback(status)
            Log.d(tag,"更新步进表$status")
        }
    }

    /**
     * 处方加载
     * */
    private fun deviceDataRecipeLoading(list: ByteArray){
        val dur = list[2].toInt() + list[3].toInt() * 256
        val flag = list[4].toInt() == 1
        cwBleRecipeLoadingCallback(flag,dur)
        Log.d(tag,"处方是否有效:$flag ,处方时长:$dur")
    }

    /**
     * 输出模式
     * */
    private fun deviceDataOutputModel(list: ByteArray){
        Log.d(tag,"设置输出模式:成功")
        cwBleSetOutputSchemeCallback(true)
    }




}