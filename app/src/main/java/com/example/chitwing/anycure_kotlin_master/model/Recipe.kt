package com.example.chitwing.anycure_kotlin_master.model
import android.util.Log
import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.Entity

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/6/5
 * QQ/Tel/Mail:383118832
 * Description:处方类
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
@Entity
class Recipe :BaseData(){
        @SerializedName("recipe_id")
        var recipeId:Int = 0
        @SerializedName("part_id")
        var partId:Int = 0
        @SerializedName("recipe_name")
        var recipeName:String? = null
        @SerializedName("recipe_icon")
        var recipeIcon:String? = null
        @SerializedName("recipe_use")
        var recipeUse:String? = null
        @SerializedName("recipe_usage")
        var recipeUsage:String? = null
        @SerializedName("recipe_help")
        var recipeHelp:String? = null
        @SerializedName("recipe_doctor_advice")
        var recipeDoctorAdvice:String? = null
        @SerializedName("recipe_text")
        var recipeText:String? = null
        var extend:String? = null
        var praise:Int = 0
        var comment:Int = 0
        var delflag:Int = 0
        var waveform:String? = null
        var frequency:String? = null
        @SerializedName("recipe_bicon")
        var recipeBigIcon:String? = null


        /**
         * 获取处方内容
         * */
        fun getRecipeContent():List<Int>{
            val set = mutableListOf<Int>()
            recipeText?.let {
                val ts = it.split(",")
                ts.forEach {
                    set.add(it.toInt())
                }

            }
            return set
        }

    fun getFinalRecipeHelp() :String?{
        val str = if (recipeHelp != null) recipeHelp!! else "默认"
        val f = str.replace(" ","")
        return  f
    }

}