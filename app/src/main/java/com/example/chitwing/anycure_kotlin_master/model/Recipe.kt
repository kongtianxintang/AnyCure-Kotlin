package com.example.chitwing.anycure_kotlin_master.model
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
        var recipeUseage:String? = null
        @SerializedName("recipe_help")
        var recipeHelp:String? = null
        @SerializedName("recipe_doctor_advice")
        var recipeDoctoraAvice:String? = null
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
}