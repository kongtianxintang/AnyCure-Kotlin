package com.example.chitwing.anycure_kotlin_master.fragment.otCure

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment


/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/8/9
 * QQ/Tel/Mail:383118832
 * Description:理疗--other渠道
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class OtCureFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_ot_cure, container, false)
        Log.d(fm_tag,"哈哈～")
        return v
    }

}
