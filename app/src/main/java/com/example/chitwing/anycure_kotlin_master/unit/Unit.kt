package com.example.chitwing.anycure_kotlin_master.unit

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.chitwing.anycure_kotlin_master.R
import android.util.TypedValue
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import com.example.chitwing.anycure_kotlin_master.ui.CWCircleTransform
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream


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
object Unit {

    fun Dp2Px(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }

    fun Px2Dp(context: Context, px: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px.toFloat(), context.resources.displayMetrics).toInt()
    }

    fun Sp2Px(context: Context, sp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), context.resources.displayMetrics).toInt()
    }

    fun Px2Sp(context: Context, px: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px.toFloat(), context.resources.displayMetrics).toInt()
    }

    fun size2sp(sp: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, context.resources.displayMetrics)
    }

    fun dip2px(dipValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 头条技术团队 屏幕自适应解决方案
     * */
    private var mNoncompatDensity:Float = 0f
    private var mNoncompatScaledDensity:Float = 0f
    fun customDensity(ac:Activity,app:Application){

        val appDisplayMetrics = app.resources.displayMetrics
        if (mNoncompatDensity == 0f){
            mNoncompatDensity = appDisplayMetrics.density
            mNoncompatScaledDensity = appDisplayMetrics.scaledDensity
        }

        val targetDensity = appDisplayMetrics.widthPixels / 360
        val targetScaledDensity = targetDensity * (mNoncompatScaledDensity / mNoncompatDensity)
        val targetDensityDpi = 160 * targetScaledDensity
        appDisplayMetrics.density = targetDensity.toFloat()
        appDisplayMetrics.scaledDensity = targetScaledDensity
        appDisplayMetrics.densityDpi = targetDensityDpi.toInt()

        val acDisplayMetrics = ac.resources.displayMetrics
        acDisplayMetrics.density = targetDensity.toFloat()
        acDisplayMetrics.scaledDensity = targetScaledDensity
        acDisplayMetrics.densityDpi = targetDensityDpi.toInt()
    }


    /**
     * zip解压缩
     * */
    fun unZip(zipFile: File, targetDir: String){
        val zFile = ZipFile(zipFile)
        val zList = zFile.entries()
        var ze: ZipEntry?
        val buffer = ByteArray(1024 * 4)
        var input: BufferedInputStream? = null
        var output: BufferedOutputStream? = null
        try {
            while (zList.hasMoreElements()){
                ze = zList.nextElement()
                if (ze.isDirectory){
                    val path = targetDir + File.separator + ze.name
                    val f = File(path)
                    f.mkdir()
                    continue
                }
                val folder = File(targetDir,ze.name)
                if (!folder.exists()){
                    folder.createNewFile()
                }
                output = BufferedOutputStream(FileOutputStream(folder))
                input = BufferedInputStream(zFile.getInputStream(ze))
                var readLen = 0

                while (input.read(buffer,0,1024).apply { readLen = this } > 0){
                    output.write(buffer,0,readLen)
                }

                input.close()
                output.close()
            }
        }catch (e: Exception){
            Log.d("unzip","解压失败")
        }
        finally {
            input?.close()
            output?.close()
        }
    }
}

/**
 * 扩展imageView 加载网路图片的方法
 * //todo:请ui给个占位图 及 错误图
 * */
fun ImageView.loader(context: Context, url:String?){
    url?.let {
        if (it.isEmpty()){ return }
        val options = RequestOptions().error(R.drawable.ic_launcher_background)
        val path = NetRequest.IMAGE_BASE_PATH + it
        Glide.with(context).load(path).apply(options).into(this)
    }
}
/**
* 扩展imageView 切圆角
 * radius 圆角
* */
fun ImageView.loadRadius(context: Context,url: String?,radius:Int){

    url?.let {
        if (it.isEmpty()){ return }
        val corners = RoundedCorners(radius)
        val options = RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_foreground).transform(corners)
        val path = NetRequest.IMAGE_BASE_PATH + it
        Glide.with(context).load(path).apply(options).into(this)
    }
}
/**
 * 扩展imageView 圆形
 * */
fun ImageView.loadCircle(context: Context,url: String?){
    url?.let {
        if (it.isEmpty()){ return }
        val options = RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).transform(CWCircleTransform())
        val path = NetRequest.IMAGE_BASE_PATH + it
        Glide.with(context).load(path).apply(options).into(this)
    }
}

/**
 * 扩展imageView 圆形 加载本地的图片
 * */
fun ImageView.loadCircle(context: Context,file: File){
    val options = RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).transform(CWCircleTransform())
    Glide.with(context).load(file).apply(options).into(this)
}

/**
 * 扩展Context方法
 * 显示toast
 * */
fun Context.showToast(text:String){
    Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
}


