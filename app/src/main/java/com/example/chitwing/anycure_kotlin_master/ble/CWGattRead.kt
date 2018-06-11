package com.example.chitwing.anycure_kotlin_master.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
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
                    Log.d(tag,"数据读取:设备发来的信息")
                    deviceData(list)
                }
                /**
                 * 发送处方返回
                 * */
                0xab -> {
                    Log.d(tag,"设备发来的信息:处方返回")
                    recipeContextSendComplete(list)
                }
                /**
                 *渠道验证
                 * */
                0xce -> {
                    Log.d(tag,"数据读取:渠道验证")
                }
                /**
                 * 幅度映射表
                 * */
                0xad -> {
                    Log.d(tag,"数据读取:幅度映射表")
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
            }
            0x05 -> {
                desc = "未知指令:05"
            }
            0x06 -> {
                desc = "处方加载"
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

    private fun batteryPower(list: ByteArray){
        if (list.count() > 2){
            val value = list[2].toInt()
            cwBleBatteryPower(value)
        }
    }

}