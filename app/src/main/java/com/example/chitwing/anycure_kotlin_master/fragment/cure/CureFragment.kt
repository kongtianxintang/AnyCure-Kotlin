package com.example.chitwing.anycure_kotlin_master.fragment.cure


import android.os.Bundle
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
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.ui.CWProgressView
import java.text.DecimalFormat


/**
 * 理疗页面
 */
class CureFragment : BaseFragment() {


    lateinit var mAdapter:CureAdapter

    /**
     *子控件
     */
    lateinit var mProgress:CWProgressView//自定义进度条
    private lateinit var mAddButton:Button//加
    private lateinit var mMinusButton:Button//减
    lateinit var mIntensityText:TextView//强度
    private lateinit var mRecyclerView:RecyclerView
    private lateinit var mFastButton:Button //一键启动
    private lateinit var mSelectRecipeButton:Button //选择处方
    private lateinit var mCountDown:TextView//倒计时
    private lateinit var mCountDownDesc:TextView // 倒计时描述
    private lateinit var mCountDownIcon:ImageView //倒计时icon
    private lateinit var mRecipeText:TextView//处方名称
    private lateinit var mFastDesc:TextView//一键启动描述
    private lateinit var mExitButton:ImageButton//推出按钮
    private lateinit var mStop:ImageButton//暂停按钮
    private lateinit var mStart:ImageButton//开始按钮
    private lateinit var mIntensityDesc:TextView//强度描述->轻微/适中/强烈
    private lateinit var mLine:View//1dp的线
    private lateinit var mDevicePower:TextView//电池电量
    private lateinit var mDeviceLink:TextView//设备连接情况


    /**
     * 数据处理
     * */
    val provider by lazy {
        return@lazy CWCureProvider(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_cure, container, false)
        initSubviews(v)
        return v
    }


    /**
     * 初始化子控件
     * */
    private fun initSubviews(v:View){
        mProgress = v.findViewById(R.id.cure_progress_view)
        mAddButton = v.findViewById(R.id.cure_add_button)
        mMinusButton = v.findViewById(R.id.cure_minus_button)
        mIntensityText = v.findViewById(R.id.cure_intensity_text)
        mIntensityText.text = "0"
        mRecyclerView = v.findViewById(R.id.cure_recycler_view)
        mFastButton = v.findViewById(R.id.cure_fast_button)
        mFastDesc = v.findViewById(R.id.cure_fast_desc)
        mSelectRecipeButton = v.findViewById(R.id.cure_select_recipe_button)
        mCountDown = v.findViewById(R.id.cure_count_down_text)
        mCountDownIcon = v.findViewById(R.id.cure_count_down_icon)
        mCountDownDesc = v.findViewById(R.id.cure_count_down_desc)
        mRecipeText = v.findViewById(R.id.cure_recipe_title)
        mExitButton = v.findViewById(R.id.cure_exit_button)
        mStop = v.findViewById(R.id.cure_stop_button)
        mStart = v.findViewById(R.id.cure_start_button)
        mIntensityDesc = v.findViewById(R.id.cure_intensity_desc)
        mLine = v.findViewById(R.id.cure_one_line)
        mDeviceLink = v.findViewById(R.id.cure_device_link)
        mDevicePower = v.findViewById(R.id.cure_device_power)

        buttonAction()
        configureRecyclerView()
    }

    /**
     * button 点击事件
     * */
    private fun buttonAction(){
        mAddButton.setOnClickListener {
            val item = mAdapter.getSelectItem()
            item?.addIntensity()
        }

        mMinusButton.setOnClickListener {
            val item = mAdapter.getSelectItem()
            item?.minusIntensity()
        }

        mExitButton.setOnClickListener {
            val item = mAdapter.getSelectItem()
            item?.endCureAction()
        }

        mStop.setOnClickListener {
            val item = mAdapter.getSelectItem()
            item?.stopCureAction()
        }

        mStart.setOnClickListener {
            val item = mAdapter.getSelectItem()
            item?.startCureAction()
        }
    }

    /**
     * 配置recyclerView
     * */
    private fun configureRecyclerView(){
        mAdapter = CureAdapter(this,CWBleManager.mCWDevices)
        mRecyclerView.adapter = mAdapter
        val layout = LinearLayoutManager(this.context!!, LinearLayoutManager.HORIZONTAL,false)
        mRecyclerView.layoutManager = layout

        mIntensityDesc.text = "轻微"

        mAdapter.setSelect(0)
        endStatus()
    }


    override fun onResume() {
        super.onResume()
        mAdapter.resetCallback()
    }

    /**
     * 暂停状态
     * */
    fun stopStatus(){
        activity!!.runOnUiThread {
            //隐藏
            mIntensityText.text = "0"
            mIntensityText.visibility = View.INVISIBLE
            mProgress.setCurrent(0)
            mFastButton.visibility = View.INVISIBLE
            mFastDesc.visibility = View.INVISIBLE
            mSelectRecipeButton.visibility = View.INVISIBLE
            mIntensityDesc.visibility = View.INVISIBLE
            mStop.visibility = View.INVISIBLE

            //显示
            mExitButton.visibility = View.VISIBLE
            mCountDown.visibility = View.VISIBLE
            mCountDownDesc.visibility = View.VISIBLE
            mCountDownIcon.visibility = View.VISIBLE
            mStart.visibility = View.VISIBLE
            mLine.visibility = View.VISIBLE
            mDeviceLink.visibility = View.VISIBLE
            mDevicePower.visibility = View.VISIBLE
        }
    }

    /**
     * 结束状态
     * */
    fun endStatus(){
        activity!!.runOnUiThread {
            //隐藏
            mIntensityText.text = "0"
            mIntensityText.visibility = View.INVISIBLE
            mProgress.setCurrent(0)
            mExitButton.visibility = View.INVISIBLE
            mCountDown.visibility = View.INVISIBLE
            mCountDownDesc.visibility = View.INVISIBLE
            mCountDownIcon.visibility = View.INVISIBLE
            mIntensityDesc.visibility = View.INVISIBLE
            mStop.visibility = View.INVISIBLE
            mLine.visibility = View.INVISIBLE
            mDeviceLink.visibility = View.INVISIBLE
            mDevicePower.visibility = View.INVISIBLE


            //显示
            mSelectRecipeButton.visibility = View.VISIBLE
            mFastDesc.visibility = View.VISIBLE
            mFastButton.visibility = View.VISIBLE
            mStart.visibility = View.VISIBLE

            mAdapter.notifyDataSetChanged()
        }

    }

    /**
     * 开始状态
     * */
    fun startStatus(){
        activity!!.runOnUiThread {
            //显示
            mIntensityText.text = "0"
            mProgress.setCurrent(0)
            mIntensityText.visibility = View.VISIBLE
            mExitButton.visibility = View.VISIBLE
            mCountDown.visibility = View.VISIBLE
            mCountDownDesc.visibility = View.VISIBLE
            mCountDownIcon.visibility = View.VISIBLE
            mStop.visibility = View.VISIBLE
            mIntensityDesc.visibility = View.VISIBLE
            mLine.visibility = View.VISIBLE
            mDeviceLink.visibility = View.VISIBLE
            mDevicePower.visibility = View.VISIBLE

            //隐藏
            mFastButton.visibility = View.INVISIBLE
            mFastDesc.visibility = View.INVISIBLE
            mSelectRecipeButton.visibility = View.INVISIBLE
            mStart.visibility = View.INVISIBLE

        }
    }

    /**
     * 切换控制器 相应变换
     * */
    fun switchDevice(item:CWDevice){
        if (item.isPlay){
            startStatus()
            setIntensity(item.intensity)
        }else{
            stopStatus()
        }
        val time = item.mDuration - item.playDuration
        setLeftTime(time)
        setPower(item.power)

    }

    /**
     * 强度相关
     * */
    fun setIntensity(value:Int){
        activity?.runOnUiThread {
            mProgress.setCurrent(value)
            mIntensityText.text = "$value"
            when(value){
                in 0 .. 6 -> mIntensityDesc.text = "轻微"
                in 7 .. 20 -> mIntensityDesc.text = "适中"
                else -> mIntensityDesc.text = "强烈"
            }
        }
    }

    /**
     * 时间相关
     * */
    fun setLeftTime(value: Int){
        activity?.runOnUiThread {
            val mm = DecimalFormat("00").format(value / 60) //分钟
            val ss = DecimalFormat("00").format(value % 60) //秒
            val str = "$mm : $ss"
            mCountDown.text = str
        }
    }
    /**
     * 电池电量相关
     * */
    fun setPower(value: Int){
        when(value) {
            in 80 .. 100 -> Log.d(fm_tag,"电池->5")
            in 50 .. 79 -> Log.d(fm_tag,"电池->4")
            in 30 .. 49 -> Log.d(fm_tag,"电池->3")
            in 10 .. 29 -> Log.d(fm_tag,"电池->2")
            else -> Log.d(fm_tag,"电池->1")
        }
    }

}
