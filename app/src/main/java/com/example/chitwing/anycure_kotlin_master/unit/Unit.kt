package com.example.chitwing.anycure_kotlin_master.unit

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.chitwing.anycure_kotlin_master.R

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/14
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class Unit {

}

/**
 * 扩展imageView 加载网路图片的方法
 * //todo:请ui给个占位图 及 错误图
 * */
fun ImageView.loader(context: Context, url:String?){
    val options = RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_foreground)
    Glide.with(context).load(url).apply(options).into(this)
}
/**
* 扩展imageView 切圆角
 * radius 圆角
* */
fun ImageView.loadRadius(context: Context,url: String?,radius:Int){
    val corners = RoundedCorners(radius)
    val options = RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_foreground).transform(corners)
    Glide.with(context).load(url).apply(options).into(this)
}

/**
 * 扩展Context方法
 * 显示toast
 * */
fun Context.showToast(text:String){
    Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
}


