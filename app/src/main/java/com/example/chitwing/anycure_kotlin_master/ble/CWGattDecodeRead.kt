package com.example.chitwing.anycure_kotlin_master.ble

import android.util.Log

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/9/12
 * QQ/Tel/Mail:383118832
 * Description:加密协议读取类
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWGattDecodeRead(delegate:CWGattReadInterface) :CWGattReadInterface by delegate{

    private val mTag = "加密协议读取类"
    /**
     * 标示 是否完成加密
     * */
    var mIsCompleteDecryption:Boolean = false
    /// cid 与外设通信的编号
    private var cid1:Int = 0
    private var cid2:Int = 0
    private var seed:Int = 0

    /**
     * 处理蓝牙数据
     * */
    fun handleData(list:ByteArray?){
        if (list == null){
            return
        }
        val first = list.firstOrNull()
        first?.let {
            if (mIsCompleteDecryption){//已完成加密工作
                normalDataProcess(list)
            }else{
                encryptionAndDecryProcess(list)
            }
        }
    }
    /**
     * 解密后的数据通信
     * */
    private fun normalDataProcess(list:ByteArray){

        list.firstOrNull()?.let {
            val first = it.toInt()
            val r = when(first) {
                in 0 until seed -> first + 256 - seed
                else -> first - seed
            }
            val ranges = list.slice(1 until list.count())
            val subs = ranges.map {
                val t = it.toInt() xor r
                if (t !in 0 .. 255){
                    t and 0xff
                }else{
                    t
                }
            }

            subs.firstOrNull()?.let {
                when (it){
                    0xa0 -> cureStatusData(subs)
                    0xa1 -> softwareSelectDevice(subs)
                    0xa2 -> softwareSetIntensity(subs)
                    0xa3 -> softwareSetOutputModel(subs)
                    0xa4 -> softwareLoadingRecipe(subs)
                    0xa5 -> softwareElectrodeQuery(subs)
                    0xa6 -> softwareBatteryPowerQuery(subs)
                    0xa7 -> softwareLockDeviceButton(subs)
                    0xa8 -> softwareRecipeSendComplete(subs)
                    0xa9 -> handleRangeMapData(subs)
                    0xaa -> softwareLEDControl(subs)
                    0xab -> softwareRemoveRecipe(subs)
                    0xac -> softwareQueryDeviceInfo(subs)
                    0xad -> softwareQuerySystemVersion(subs)
                    0x50 -> hardwareElectrodeNotify(subs)
                    0x51 -> hardwarePlayComplete(subs)
                    0x52 -> hardwareExtensionElectrodeNotify(subs)
                    0x53 -> hardwareIntensityNotify(subs)
                    0x54 -> hardwareIntensityAutoIncrementComplete(subs)
                    0x55 -> hardwareOutputNotify(subs)
                    0x56 -> hardwareUnlockDeviceButton(subs)
                    0x57 -> hardwareCloseNotify(subs)
                    0x58 -> hardwareBatteryPowerNotify(subs)
                    else -> {Log.d(mTag,"未知指令 cmd:$it")}
                }
            }

        }
    }

    /**
     * 加解密过程
     * */
    private fun encryptionAndDecryProcess(list: ByteArray){
        val subs = list.map { if (it.toInt() !in 0 .. 255){ it.toInt() and 0xff}else { it.toInt()} }
        subs.firstOrNull()?.let {
            when (it){
                0x5a -> {//第一步 设备返回的10个随机数
                    deviceNormalRandom(subs)
                }
                0x5b -> {//第二步 设备返回的10个经过加密的随机数
                    deviceEncryptionRandom(subs)
                }
                0x0a -> {//重置密钥 及 获取通信编号(明文传输)
                    plaintextData(subs)
                }
                else -> {
                    Log.d(mTag,"加密解密过程未知cmd->$it")
                }
            }

        }
    }

    private fun deviceNormalRandom(list: List<Int>){
        Log.d(mTag,"第一步 设备返回的10个随机数")
        val decodes = mutableListOf<Int>()
        for (i in 1 until list.count()){
            val t = list[i]
            val num = when(i){
                in 1 .. 5 -> t xor 0x55
                else -> t xor 0xaa
            }
            decodes.add(num)
        }
        val finalCodes = mutableListOf<Int>()
        val channelCodes = CWBleManager.configure.channel.channelCode
        val count = channelCodes.count()
        for (i in 0 until decodes.count()){
            val decode = decodes[i]
            if (count > i){
                val code = channelCodes[i]
                val final = code xor decode
                finalCodes.add(final)
            }else {
                val final = decode xor 0xa5
                finalCodes.add(final)
            }
        }
        cwBleDeviceNormalRandom(finalCodes.toTypedArray())
    }

    private fun deviceEncryptionRandom(list: List<Int>){
        Log.d(mTag,"第三步:处理设备发来的10个加密过的随机数")
        val randoms = mutableListOf<Int>()
        for (i in 1 until list.count()){
            val num = list[i]
            val final = when(i) {
                in 1 .. 5 -> num xor 0xf0
                else -> num xor 0x0f
            }
            when(final) {
                in 0 until  0x80 ->{
                    val t = final + 256 - 0x80
                    randoms.add(t)
                }
                else -> {
                    val t = final - 0x80
                    randoms.add(t)
                }
            }
        }
        if (randoms.count() > 9){
            val n = randoms[9] % 10
            Log.d(mTag,"计算cid 原始数据->$n")
            cid1 = (randoms[(n + 1) % 10] + 11) % 256 or 0x80
            cid2 = (randoms[(n + 2) % 10] + 17) % 256 or 0x08
            seed = randoms[n]
            Log.d(mTag,"验证cid1->$cid1 cid2->$cid2 seed->$seed")
            mIsCompleteDecryption = true
            cwBleCalculate(seed)
        }
    }

    /**
     * 明文数据处理
     * */
    private fun plaintextData(list: List<Int>){
        if (list.count() > 1){
            val cmd = list[1]
            when (cmd){
                0x01 -> deviceResetSeed(list)
                0x02 -> devicePlaySerialNumber(list)
                else -> { Log.d(mTag,"明文通信未知cmd->$cmd") }
            }
        }
    }

    /**
     * 重置密钥
     * */
    private fun deviceResetSeed(list: List<Int>){
        if (list.count() > 2){
            val flag = list[2].toInt() == 0
            Log.d(mTag,"重置密钥->$flag")
            cwBleDeviceResetSeed(flag)
        }
    }
    /**
     * 获取通信编号
     * */
    private fun devicePlaySerialNumber(list: List<Int>){
        if (list.count() > 4){
            val flag = list[2] == 1
            Log.d(mTag,"验证通信编号$flag")
            if (flag){
                val tCid1 = list[3]
                val tCid2 = list[4]
                Log.d(mTag,"验证通信编号tCid1->$tCid1,tCid2->$tCid2")
                if (tCid1 == cid1 && tCid2 == cid2){
                    mIsCompleteDecryption = true
                    cwBleCommunicationSerialNumber(true)
                }else{
                    cwBleCommunicationSerialNumber(false)
                }
            }else{
                cwBleCommunicationSerialNumber(false)
            }
        }
    }

    /**
     * 理疗状态数据
     * */
    private fun cureStatusData(list: List<Int>){
        if (list.count() > 1){
            val cmd = list[1]
            when (cmd) {
                0x01 -> resumeCure(list)
                0x02 -> stopCure(list)
                0x03 -> endCure(list)
                else -> { Log.d(mTag,"理疗状态数据 未知指令->$cmd") }
            }
        }
    }

    private fun resumeCure(list: List<Int>){
        if (list.count() > 2){
            val flag = list[2] == 0
            cwBleSoftwareStartCureCallback(flag)
        }
    }

    private fun stopCure(list: List<Int>){
        if (list.count() > 2){
            val flag = list[2] == 0
            cwBleSoftwareStopCureCallback(flag)
        }
    }

    private fun endCure(list: List<Int>){
        if (list.count() > 2){
            val flag = list[2] == 0
            cwBleSoftwareEndCure(flag)
        }
    }

    /**
     * 软件 选择设备
     * */
    private fun softwareSelectDevice(list: List<Int>){
        if (list.count() > 1){
            val flag = list[1] == 0x00
            cwBleSelectDeviceCallback(flag)
        }
    }

    /**
     * 软件 设置强度
     * */
    private fun softwareSetIntensity(list: List<Int>){
        if (list.count() > 2){
            val flag = list[1] == 0x00
            val value = list[2]
            cwBleSetIntensityCallback(flag = flag,value = value)
        }
    }
    /**
     * 软件 输出模式
     * */
    private fun softwareSetOutputModel(list: List<Int>){
        if (list.count() > 1){
            val flag = list[1] == 0x00
            cwBleSetOutputSchemeCallback(flag)
        }
    }

    /**
     * 软件 处方加载
     * */
    private fun softwareLoadingRecipe(list: List<Int>){
        if (list.count() > 5){
            val low = list[4]
            val high = list[5]
            val dur = low + high * 256
            val id = list[3]
            val flag = list[2]  == 0x00
            Log.d(mTag,"处方加载 是否有效:$flag 处方id:$id 处方时间:$dur high:$high low:$low")
            cwBleRecipeLoadingCallback(flag,dur)
        }
    }

    ///软件 电极贴贴合状态查询
    private fun softwareElectrodeQuery(list: List<Int>){
        if (list.count() > 5) {
            val isInsert = list[2] == 1//0未插入 1插入
            val main = list[3]
            val e1 = list[4]
            val e2 = list[5]
            Log.d(mTag,"软件 电极贴合状态查询: $main")
            cwBleElectrodeQueryCallback(isInsert,main,e1,e2)
        }
    }
    ///硬件 电极贴状态主动通知
    private fun hardwareElectrodeNotify(bytes:List<Int>){
        if (bytes.count() > 4) {
            val isInsert = bytes[1] == 1//0未插入 1插入
            val main = bytes[2]
            val e1 = bytes[3]
            val e2 = bytes[4]
            Log.d(mTag,"硬件通知 电极贴合状态 $main")
            cwBleElectrodeNotify(false,isInsert,main,e1,e2)
        }
    }

    ///硬件 播放完成通知
    private fun hardwarePlayComplete(bytes:List<Int>){
        cwBlePlayCompleteNotify(true)
    }


    /**
     * 电量查询
     * */
    private fun softwareBatteryPowerQuery(list: List<Int>){
        if (list.count() > 1){
            val power = list[1]
            cwBleBatteryPower(power)
        }
    }

    /**
     * 硬件 电池电量通知
     * */
    private fun hardwareBatteryPowerNotify(list: List<Int>){
        if (list.count() > 1){
            val power = list[1]
            cwBleBatteryPower(power)
        }
    }

    /**
     * 硬件 扩展点击状态变化通知
     * */
    private fun hardwareExtensionElectrodeNotify(list: List<Int>){
        if (list.count() > 1){
            val flag =  list[1] == 0x01
            cwBleExtensionElectrodeInsertNotify(flag)
        }
    }

    /**
     * 硬件 输出强度变化通知
     * */
    private fun hardwareIntensityNotify(list: List<Int>){
        if (list.count() > 1){
            val value = list[1]
            cwBleIntensityNotify(value)
        }
    }

    /**
     * 硬件 输出开始or暂停通知
     * */
    private fun hardwareOutputNotify(list: List<Int>){
        if (list.count() > 1){
            val flag = list[1] == 0x01
            cwBleCureStatusNotify(flag)
        }
    }

    /**
     * 软件 获取设备状态信息 :是否播放 已经播放时长
     * */
    private fun softwareQueryDeviceInfo(list: List<Int>){
        if (list.count() > 5) {
            val isPlay = list[1] == 0x01
            val intensity = list[2]
            val id = list[3]
            val dur = list[4] +  list[5] * 256
            cwBleDeviceStatusQueryCallback(isPlay,intensity,id,dur)
            Log.d(mTag,"获取设备状态: 是否在播放->$isPlay 强度->$intensity 处方id->$id 播放时长->$dur")
        }
    }

    /**
     * 软件 锁定设备按钮
     * */
    private fun softwareLockDeviceButton(list: List<Int>){
        if (list.count() > 1){
            val flag = list[1] == 0x01
            cwBleLockDeviceButtonCallback(flag)
        }
    }

    /**
     * 硬件 手动解锁设备按钮
     * */
    private fun hardwareUnlockDeviceButton(list: List<Int>){
        if (list.count() > 1){
            val flag = list[1] == 1
            cwBleUnlockDeviceButtonNotify(flag)
        }
    }

    /**
     * 软件 LED灯控制
     * */
    private fun softwareLEDControl(list: List<Int>){
        if (list.count() > 2){
            val flag = list[2] == 0
            cwBleControlLed(flag)
        }
    }

    /**
     * 软件 删除处方
     * */
    private fun softwareRemoveRecipe(list: List<Int>){
        if (list.count() > 2){
            val flag = list[2] == 0
            cwBleRemoveRecipe(flag)
        }
    }

    /**
     * 软件 查询系统版本号
     * */
    private fun softwareQuerySystemVersion(list: List<Int>){
        if (list.count() > 3){
            val low = list[2]
            val high = list[3] * 256
            val v = low + high
            cwBleDeviceSystemVersion(v)
        }
    }

    /**
     * 硬件 输出幅度自动增加完成
     * */
    private fun hardwareIntensityAutoIncrementComplete(list: List<Int>){
        cwBleIntensityAutoIncrementComplete()
    }

    /**
     * 硬件 设备关机
     * */
    private fun hardwareCloseNotify(list: List<Int>){
        if (list.count() > 1){
            val v = list[1]// 0x01 ->按键关机 0x02 ->未操作超时关机 0x03 ->充电关机 0x04 -> 低电量关机
            cwBleDeviceClose(v)
        }
    }

    /**
     * 发送处方
     * */
    private fun softwareRecipeSendComplete(list: List<Int>){
        if (list.count() > 2){
            val index = list[1]
            val total = list[2]
            Log.d(mTag,"处方发送 第几帧->$index 合计->$total")
            cwBleRecipeSendIndexCallback(index,total)
        }
    }

    /**
     * 幅度映射表 相关
     * */
    private fun handleRangeMapData(list: List<Int>){
        if (list.count() > 1){
            val cmd = list[1]
            when (cmd) {
                0x01 -> rangeMapVersion(list)
                0x02 -> rangeMapContentWrite(list)
                0x03 -> rangeMapUpdate(list)
                0x04 -> rangeMapRemove(list)
                else -> { Log.d(mTag,"映射表 未知指令 $cmd") }
            }
        }
    }

    /**
     * 映射表 版本号
     * */
    private fun rangeMapVersion(list: List<Int>){
        if (list.count() > 3){
            val ver = list[2]
            val steps = list[3]
            Log.d(mTag,"映射表版本号:$ver 步进数:$steps")
            cwBleDeviceRangeMapCallback(ver,steps)
        }
    }

    /**
     * 映射表 写入
     * */
    private fun rangeMapContentWrite(list: List<Int>){
        if (list.count() > 3){
            val index = list[2]
            val flag = list[3] == 0
            cwBleDeviceRangeMapWriteCallback(index,flag)
        }
    }
    /**
     * 映射表 升级
     * */
    private fun rangeMapUpdate(list: List<Int>){
        if (list.count() > 2){
            val flag = list[2] == 0
            cwBleDeviceRangeMapUpdateCallback(flag)
        }
    }
    /**
     * 映射表 删除
     * */
    private fun rangeMapRemove(list: List<Int>){
        if (list.count() > 2){
            val flag = list[2] == 0
            cwBleRangeMapRemove(flag)
        }
    }

}