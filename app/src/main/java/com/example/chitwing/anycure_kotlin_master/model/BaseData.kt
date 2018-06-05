package com.example.chitwing.anycure_kotlin_master.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/5
 * QQ/Tel/Mail:383118832
 * Description:保存到数据库的基础类
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
@Entity
abstract class BaseData {
    @Id var id:Long = 0
}