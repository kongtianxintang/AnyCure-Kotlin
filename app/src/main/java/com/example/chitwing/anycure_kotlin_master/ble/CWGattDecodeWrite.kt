package com.example.chitwing.anycure_kotlin_master.ble

import android.util.Log
import java.util.*

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/9/12
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWGattDecodeWrite(delegate: CWGattWriteInterface) :CWGattWriteInterface by delegate{

    private val mTag = "加密数据写入类"
    var seed: Int = 0 //密钥


    /*************** 新协议 ***************/
    /**
     * 设备解锁过程 1.向设备发送解锁启动指令
     * */
    fun cwBleWriteDecryptionCMD(){
        val data = listOf(0xa5,0x75,0x6e,0x6c,0x6f,0x63,0x6b,0x69,0x6e,0x67)
        cwGattWriteData(data)
    }
    /**
     * 设备解锁过程 2.向设备发送经过解码后的内容
     * */
    fun cwBleWriteEncryptionRandomWith(list: Array<Int>){
        val data = list.toMutableList()
        data.add(0,0xa5)
        cwGattWriteData(data)
    }



    /**
     * 开始理疗 - 开始输出
     * */
    fun cwBleWriteStartCure(){
        val list = listOf(0xa0,0x01)
        decodeData(list)
    }

    /**
     * 暂停输出
     * */
    fun cwBleWriteStopCure(){
        val data = listOf(0xa0,0x02)
        decodeData(data)
    }

    /**
     * 停止输出
     * */
    fun cwBleWriteEndCure(){
        val list = listOf(0xa0,0x03)
        decodeData(list)
    }

    /**
     * 选择设备
     * 0x00 为取消 0x01为选择
     * */
    fun cwBleWriteSelectDevice(value: Int){
        val cmd = if (value == 1) 0x01 else 0x00
        //0x00 为取消选择 0x01 为选择
        val data = listOf(0xa1,cmd)
        decodeData(data)
    }

    /**
     * 调节强度
     * */
    fun cwBleWriteIntensity(value: Int){
        val data = listOf(0xa2,value)
        decodeData(data)
    }

    /**
     * 设置输出模式
     * 00关闭输出 01单电极输出 02双电极输出
     * */
    fun cwBleWriteOutputModel(value:Int){
        val subs = listOf(0xa3,value)
        decodeData(subs)
    }

    /**
     * 加载处方
     * */
    fun cwBleWriteLoadingRecipe(){
        val commands = listOf(0xa4,0x01)
        decodeData(commands)
    }

    /**
     *电极贴状态查询
     * */
    fun cwBleWriteElectrodeQuery(){
        val data = listOf(0xa5,0x01)
        decodeData(data)
    }

    /**
     * 电量查询
     * */
    fun cwBleWriteBatteryPowerQuery(){
        val list = listOf(0xa6,0x01)
        decodeData(list)
    }

    /**
     * 设备按钮锁定
     * */
    fun cwBleWriteLockDeviceButton(flag: Boolean){
        val cmd = if (flag) 0x01 else 0x00
        val list = listOf(0xa7,cmd)
        decodeData(list)
    }

    /**
     * 写入处方
     * */
    fun cwBleWriteRecipeContent(index:Int,recipeContent:List<Int>?){
        recipeContent?.let {
            val max = it.count() / 12
            if (index > max){
                return
            }
            val subs = it.subList((index - 1) * 12,index * 12)
            var contentIndex = index
            if (index == max){
                contentIndex = 0x00
            }
            val content = mutableListOf(0xa8,contentIndex)
            content.addAll(subs)
            decodeData(content.toList())
        }
    }

    /**
     * 幅度映射表 查询
     * */
    fun cwBleWriteQueryRangeMap(){
        val data  = listOf(0xa9,0x01)
        decodeData(data)
    }

    /**
     * 幅度映射表 写入
     * todo:未验证～请做测试
     * */
    fun cwBleWriteRangeMapContent(index: Int){
        val content = CWBleManager.configure.channel.rangeMap
        val max = content.count() / 16
        if (index >= max){
            return
        }
        val subs = content.subList(index * 16,(index + 1) * 16)
        val data = mutableListOf(0xa9,0x02,index)
        data.addAll(subs)
        decodeData(data)
    }

    /**
     * 幅度映射表 更新
     * */
    fun cwBleWriteRangeMapUpdate(){
        val list = listOf(0xa9,0x3)
        decodeData(list)
    }

    /**
     * 幅度映射表 删除
     * */
    fun cwBleWriteRemoveRangeMap(){
        val list = listOf(0xa9,0x04)
        decodeData(list)
    }

    /**
    led灯控制

    @param color led灯的颜色 0熄灭 1红色 2绿色 3蓝色 4黄色 5紫色 6青色 7白色
    @param num 闪烁次数
    @param dur 点亮时间 / 熄灭时间
    @param cbp 外设
    @param characteristic 外设特征值
     */
    fun cwBleWriteControlLed(color: Int, num: Int, brightDur: Int, extinguishDur: Int){
        val list = listOf(0xaa,0x01,color,num,brightDur,extinguishDur)
        decodeData(list)
    }

    /**
     * 删除内部处方
     * */
    fun cwBleWriteRemoveDeviceRecipe(){
        val list = listOf(0xab,0x01)
        decodeData(list)
    }

    /**
     * 设备状态查询
     * */
    fun cwBleWriteDeviceStatusQuery(){
        val data = listOf(0xac,0x01)
        decodeData(data)
    }

    /**
     * 查询系统版本号
     * */
    fun cwBleWriteSystemVersion(){
        val list = listOf(0xad,0x01)
        decodeData(list)
    }

    /**
    重置硬件密钥
     */
    fun cwBleWriteRestSeed(){
        val list = listOf(0x1a,0x01)
        cwGattWriteData(list)
    }

    /**
    断后重联获取下播放信息
    与获取设备信息不同
     */
    fun cwBleWriteCommunicationSerialNumber(){
        val list = listOf(0x1a,0x02)
        cwGattWriteData(list)
    }


    /**
     * 对数据加密
     * */
    private fun decodeData(list: List<Int>) {
        val n = Random().nextInt(256)
        val ec = n + seed
        val subs = list.map {
            it xor n
        }
        val mutableList = subs.toMutableList()
        mutableList.add(0,ec)
        val target = mutableList.toList()
        cwGattWriteData(target)
    }
}