package com.example.chitwing.anycure_kotlin_master.fragment.otCure

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
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
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.ble.CWDevice
import com.example.chitwing.anycure_kotlin_master.dialog.CWDialog
import com.example.chitwing.anycure_kotlin_master.dialog.CWDialogInterface
import com.example.chitwing.anycure_kotlin_master.dialog.CWDialogType
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.unit.loader
import java.text.DecimalFormat


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

    //子控件
    private lateinit var mRecyclerView:RecyclerView
    private lateinit var mIcon:ImageView
    private lateinit var mLinkTitle:TextView
    private lateinit var mLinkDesc:TextView
    private lateinit var mPowerIcon:ImageView
    private lateinit var mAddButton: ImageButton
    private lateinit var mMinusButton:ImageButton
    private lateinit var mIntensityLabel:TextView
    private lateinit var mResumeButton: ConstraintLayout
    private lateinit var mExitButton:ConstraintLayout
    private lateinit var mCountdown:TextView
    private lateinit var mStopButton:ConstraintLayout
    private lateinit var mRecipeName:TextView

    //数据处理类
    private val mProvider:OtCureProvider by lazy {
        return@lazy OtCureProvider(this)
    }
    //当前的设备
    private var mCurrentDevice:CWDevice? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_ot_cure, container, false)
        v?.let {
            initSubviews(v)
        }
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureButtonAction()
        initAdapter()
        endStatus()
        defaultItem()
    }


    /**
     * 绑定子控件
     * */
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
        mStopButton = v.findViewById(R.id.ot_cure_stop_button)
        mRecipeName = v.findViewById(R.id.ot_cure_recipe_name)


    }

    private fun initAdapter(){
        val layout = LinearLayoutManager(this.context!!, LinearLayoutManager.HORIZONTAL,false)
        mRecyclerView.layoutManager = layout
        val adapter = OtCureAdapter(R.layout.ot_cure_item,CWBleManager.mCWDevices)

        adapter.setOnItemClickListener { _, _, position ->
            Log.d("点击","位置->$position")
            val count = CWBleManager.mCWDevices.count()
            if (count > position){
                val obj = CWBleManager.mCWDevices[position]
                switchItem(obj)
            }
        }
        mRecyclerView.adapter = adapter


    }

    /**
     * button 的点击事件
     * */
    private fun configureButtonAction(){
        mResumeButton.setOnClickListener {
            mCurrentDevice?.startCureAction()
        }
        mStopButton.setOnClickListener {
            mCurrentDevice?.stopCureAction()
        }
        mExitButton.setOnClickListener {
            userEndCure()
        }
        mAddButton.setOnClickListener {
            mCurrentDevice?.addIntensity()
        }
        mMinusButton.setOnClickListener {
            mCurrentDevice?.minusIntensity()
        }

    }

    private fun defaultItem(){
        val last = CWBleManager.mCWDevices.lastOrNull()
        last?.let {
            switchItem(it)
        }
    }
    /**
     * 结束状态
     * */
    fun endStatus(){
        activity?.runOnUiThread {
            mLinkTitle.text = "正常"
            mLinkDesc.text = "设备连接正常"
            mPowerIcon.setImageResource(R.mipmap.ot_power_lev_1)
            configureAddAndMinus(false)
            mIntensityLabel.text = "0"
            mCountdown.setText(R.string.default_clock_num)
            configureResumeButton(true)
        }
    }
    /**
     * 暂停状态
     * */
    fun stopStatus(){
        activity?.runOnUiThread {
            configureAddAndMinus(false)
            configureResumeButton(true)
        }
    }
    /**
     * 播放状态
     * */
    fun playStatus(){
        activity?.runOnUiThread {
            configureAddAndMinus(true)
            configureResumeButton(false)
        }
    }

     private fun configureAddAndMinus(flag:Boolean){
        activity?.runOnUiThread {
            mAddButton.isEnabled = flag
            mMinusButton.isEnabled = flag
            when (flag) {
                true -> {
                    mAddButton.setImageResource(R.mipmap.ot_add)
                    mMinusButton.setImageResource(R.mipmap.ot_minus)
                }
                else -> {
                    mAddButton.setImageResource(R.mipmap.ot_add_disable)
                    mMinusButton.setImageResource(R.mipmap.ot_minus_disable)
                }
            }
        }
    }

    /**
     * 切换设备
     * */
    fun switchItem(obj:CWDevice){
        mCurrentDevice?.selectDevice(false)
        mCurrentDevice?.mCallback = null
        obj.selectDevice(true)
        obj.mCallback = mProvider.callback
        mCurrentDevice = obj
        when (obj.isPlay){
            true -> playStatus()
            else -> stopStatus()
        }
        setIntensityNum(obj.intensity)
        setConnect(obj.isConnect)
        setPowerIcon(obj.power)
        setRecipeIcon(obj.recipe?.recipeBigIcon)
        setRecipeName(obj.recipe?.recipeName)
        setPlayDuration(obj.playDuration)
    }

    /// 配置resumeButton的 文字及 图标
    ///
    /// - Parameter flag: flag ->true 代表暂停状态 flag ->false 代表播放状态
    private fun configureResumeButton(flag:Boolean){
        if (flag) {
            mResumeButton.visibility = View.VISIBLE
            mStopButton.visibility = View.INVISIBLE
        }else {
            mResumeButton.visibility = View.INVISIBLE
            mStopButton.visibility = View.VISIBLE
        }
    }

    fun setIntensityNum(arg:Int){
        activity?.runOnUiThread {
            mIntensityLabel.text = "$arg"
        }
    }

    private fun setRecipeIcon(path:String?){
        activity?.runOnUiThread {
            mIcon.loader(activity!!,path)
        }
    }

    private fun setRecipeName(name:String?){
        activity?.runOnUiThread {
            mRecipeName.text = name
        }
    }

    fun setPowerIcon(arg:Int){
        val resId = when(arg){
            in 80 .. 100 -> R.mipmap.ot_power_lev_1
            in 50 .. 79 -> R.mipmap.ot_power_lev_2
            in 30 .. 49 -> R.mipmap.ot_power_lev_3
            in 10 .. 29 -> R.mipmap.ot_power_lev_4
            else -> R.mipmap.ot_power_lev_5
        }
        activity?.runOnUiThread {
            mPowerIcon.setImageResource(resId)
        }
    }

    fun setConnect(flag:Boolean){
        activity?.runOnUiThread {
            val str = if (flag) "正常" else "异常"
            val color = if (flag) Color.BLACK else Color.RED
            mLinkTitle.text = str
            mLinkTitle.setTextColor(color)
        }
    }

    fun setPlayDuration(arg: Int){
        activity?.runOnUiThread {
            val mm = DecimalFormat("00").format(arg / 60) //分钟
            val ss = DecimalFormat("00").format(arg % 60) //秒
            val str = "$mm : $ss"
            mCountdown.text = str
        }
    }

    /**
     * 理疗中结束
     * */
    private fun userEndCure(){
        val dialog = CWDialog.Builder().setTitle("提示").setDesc("你确定要停止理疗吗?").create()
        dialog.setCallback(mDialogCallback)
        dialog.show(activity!!.fragmentManager,"end_cure")
    }

    private val mDialogCallback = object : CWDialogInterface {
        override fun onClickButton(flag: Boolean, item: CWDialog) {
            if (flag) {
                when (item.getType()){
                    CWDialogType.Other -> {
                        mCurrentDevice?.endCureAction()
                    }
                    CWDialogType.Error -> {

                    }
                    CWDialogType.Hint -> {

                    }
                }
            }
            item.dismiss()
        }
    }

}
