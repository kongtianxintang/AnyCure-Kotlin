package com.example.chitwing.anycure_kotlin_master.fragment.otCure

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.ble.CWDevice
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

    private lateinit var mRecyclerView:RecyclerView
    private lateinit var mIcon:ImageView
    private lateinit var mLinkTitle:TextView
    private lateinit var mLinkDesc:TextView
    private lateinit var mPowerIcon:ImageView
    private lateinit var mAddButton: ImageButton
    private lateinit var mMinusButton:ImageButton
    private lateinit var mIntensityLabel:TextView
    private lateinit var mResumeButton:Button
    private lateinit var mExitButton:Button
    private lateinit var mCountdown:TextView
    private var mIsResume:Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_ot_cure, container, false)
        v?.let {
            initSubviews(v)
        }
        return v
    }

    private fun initSubviews(v:View){
        mRecyclerView = v.findViewById(R.id.ot_cure_recycler_view)
        mIcon = v.findViewById(R.id.ot_cure_icon)
        mLinkTitle = v.findViewById(R.id.ot_cure_link_status)
        mLinkDesc = v.findViewById(R.id.ot_cure_link_desc)
        mPowerIcon = v.findViewById(R.id.ot_cure_power_icon)
        mAddButton = v.findViewById(R.id.ot_cure_add_button)
        mMinusButton = v.findViewById(R.id.ot_cure_minus_button)
        mIntensityLabel = v.findViewById(R.id.ot_cure_intensity_num)
        mResumeButton = v.findViewById(R.id.ot_cure_resume_button)
        mExitButton = v.findViewById(R.id.ot_cure_exit_button)
        mCountdown = v.findViewById(R.id.ot_cure_countdown)
    }

    fun endStatus(){
        activity?.runOnUiThread {
            mLinkTitle.text = "正常"
            mLinkDesc.text = "设备连接正常"
            mPowerIcon.setImageResource(R.mipmap.power_lev_1)
            configureAddAndMinus(false)
            mIntensityLabel.text = "0"
            mCountdown.setText(R.string.default_clock_num)
        }
    }
    fun stopStatus(){
        activity?.runOnUiThread {
            configureAddAndMinus(false)
            configureResumeButton(true)
        }
    }
    fun playStatus(){
        activity?.runOnUiThread {
            configureAddAndMinus(true)
            configureResumeButton(false)
        }
    }

    fun configureAddAndMinus(flag:Boolean){
        activity?.runOnUiThread {
            mAddButton.isEnabled = flag
            mMinusButton.isEnabled = flag
        }
    }

    fun switchItem(obj:CWDevice){

    }

    /// 配置resumeButton的 文字及 图标
    ///
    /// - Parameter flag: flag ->true 代表暂停状态 flag ->false 代表播放状态
    private fun configureResumeButton(flag:Boolean){
        mIsResume = flag
        //todo:需要配置开始or暂停按钮
//        val colorResId = if (flag) R.color.main else R.color.app_gray
//        val strResId = if (flag) R.string.default_resume_desc else R.string.default_stop_desc
    }

}
