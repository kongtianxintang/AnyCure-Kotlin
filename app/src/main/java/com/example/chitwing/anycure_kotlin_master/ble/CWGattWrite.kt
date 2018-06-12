package com.example.chitwing.anycure_kotlin_master.ble

import android.os.AsyncTask
import android.os.Handler
import android.util.Log
import java.util.*

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/11
 * QQ/Tel/Mail:383118832
 * Description:数据写入类
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWGattWrite(b:CWGattWriteInterface) :CWGattWriteInterface by b {
    private val tag = "数据写入类"

    /**
     * 写入渠道号
     * */
    fun cwBleWriteChannelCode(codes:List<Int>){
        val maps = mutableListOf<Int>()
        val f = 0xec
        val s = 0x11
        maps.addAll(codes)
        maps.add(0,s)
        maps.add(0,f)
        cwGattWriteData(maps)
    }

    /**
     * 根据渠道号返回的随机数写入密钥
     * */
    fun cwBleWriteACK(list: List<Int>){

        if (list.count() <= 8){
            return
        }
        //加密种子
        val x0 = 0x32
        val x1 = 0x32
        val x2 = 0x54
        val x3 = 0x4d
        val x4 = 0x61
        val x5 = 0x79
        val x6 = 0x23
        val x7 = 0x26
        val x8 = 0x47

        //收到硬件的随机数
        val a0 = list[4]
        val b0 = list[0]
        val b1 = list[1]
        val b2 = list[2]
        val b3 = list[3]
        val b4 = list[5]
        val b5 = list[6]
        val b6 = list[7]
        val b7 = list[8]

        //产生随机数变种
        val c1 = a0 + x0
        val d0 = b0 + x1
        val d1 = b1 + x2
        val d2 = b2 + x3
        val d3 = b3 + x4
        val d4 = b4 + x5
        val d5 = b5 + x6
        val d6 = b6 + x7
        val d7 = b7 + x8

        //随机数变种 异或
        val f0 = d0 xor (a0 + 2)
        val f1 = d1 xor (a0 + 3)
        val f2 = d2 xor (a0 + 7)
        val f3 = d3 xor (a0 + 19)
        val f4 = d4 xor (a0 + 23)
        val f5 = d5 xor (a0 + 29)
        val f6 = d6 xor (a0 + 31)
        val f7 = d7 xor (a0 + 41)

        //产生随机填充数
        val n = Random().nextInt(100)
        Log.d(tag,"随机数$n")
        val n1 = n + 4
        val n2 = n + 6
        val n3 = n + 8

        val datas = mutableListOf(0xec, 0x12)
        datas.add(n)
        datas.add(f1)
        datas.add(f0)
        datas.add(n1)
        datas.add(f2)
        datas.add(n2)
        datas.add(c1)
        datas.add(f3)
        datas.add(n3)
        datas.add(f4)
        datas.add(f5)
        datas.add(f6)
        datas.add(f7)

        cwGattWriteData(datas)
    }

    /**
     * 查询步进表
     * */
    fun cwBleWriteQueryRangeMap(){
        val data  = listOf(0xad,0x01)
        cwGattWriteData(data)
    }

    /**
     * 写入处方
     * */
    fun cwBleWriteRecipeContent(index:Int,recipeContent:List<Int>){

        val max = recipeContent.count() / 12
        if (index > max){
            return
        }
        val subs = recipeContent.subList((index - 1) * 12,index * 12)
        var contentIndex = index
        if (index == max){
            contentIndex = 0x00
        }
        Log.d(tag,"处方写入 $index,max：$max")
        val content = mutableListOf(0xab,0x00,contentIndex)
        content.addAll(subs)
        cwGattWriteData(content)

    }
    /**
    * 加载处方
    * */
    fun cwBleWriteLoadingRecipe(){
        val commands = listOf(0xaa, 0x06, 0x01)
        cwGattWriteData(commands)
    }

    /**
     * 设置输出模式
     * 00关闭输出 01单电极输出 02双电极输出
     * */
    fun cwBleWriteOutputModel(value:Int){
        val subs = listOf(0xaa,0x04,value)
        cwGattWriteData(subs)
    }



    private var writeTask:WriteContentTask? = null

    /**
     * 异步线程写入处方内容
     * */
    inner class WriteContentTask internal constructor(private val index: Int,private val recipeContent:List<Int>) : AsyncTask<Void, Void, Boolean>(){
        override fun doInBackground(vararg params: Void): Boolean? {
            val max = recipeContent.count() / 12 - 1

            for (i in 0 .. max){
                var contentIndex = i + 1
                if (i == max){
                    contentIndex = 0x00
                }
                val subs = recipeContent.subList(i * 12,(i + 1) * 12)
                val content = mutableListOf(0xab,0x00,contentIndex)
                content.addAll(subs)
                Thread.sleep(50)
                cwGattWriteData(content)
            }
            return true
        }


        override fun onPostExecute(success: Boolean?) {
            writeTask = null
        }

        override fun onCancelled() {
            writeTask = null
        }
    }



}