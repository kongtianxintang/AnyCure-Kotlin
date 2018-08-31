package com.example.chitwing.anycure_kotlin_master.activity.register


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.model.SMSCode
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import kotlinx.android.synthetic.main.fragment_sm.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class SMSFragment : BaseFragment() {
    private val mTag = "获取验证码"

    /**
     * 倒计时
     * */
    private var mTimer:Timer? = null
    private var mTimerTask:TimerTask? = null
    private var mCount = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sm, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        defaultSubviews()
        fetchCode()
    }

    /**
     * 配置子控件基础内容
     * */
    private fun defaultSubviews(){
        defaultSMSStatus()
        smsCountdown.visibility = View.INVISIBLE
        smsRegain.visibility = View.INVISIBLE

        smsRegain.setOnClickListener {
            fetchCode()
        }
    }

    /**
     * 网络请求code
     * */
    private fun fetchCode(){
        val ac = activity as? CWRegisterActivity
        val type = activity?.intent?.extras?.getInt("type")
        val map = mutableMapOf<String,String>()
        val typeStr = if (type == 1) "01" else "02"
        map["type"] = typeStr
        map["mobile"] = ac!!.phone!!
        map["channel"] = "00000006"
        //todo:发送忘记密码 验证码时提示发送失败 请与iOS对比
        val call = NetRequest.fetchSMSCode(map)
        call.enqueue( object :Callback<SMSCode> {
            override fun onResponse(call: Call<SMSCode>?, response: Response<SMSCode>?) {
                response?.body()?.let {
                    Log.d(mTag,"${it.code}")
                    when(it.code){
                        0 -> {
                            Log.d(mTag,"验证码->${it.data?.code}")
                            fetchSuccess(it)
                        }
                        1 -> {
                            Log.d(mTag,"错误信息->${it.msg} map->$map")
                            fetchFailure()
                        }
                        else -> {
                            fetchFailure()
                        }
                    }
                }

            }

            override fun onFailure(call: Call<SMSCode>?, t: Throwable?) {
                fetchFailure()
            }
        })

    }

    private fun defaultSMSStatus(){
        getAC()?.let {
            it.smsStatus.text = "正在发送短信到${it.phone}"
        }
    }
    /**
     * 获取成功
     * */
    private fun fetchSuccess(obj:SMSCode){
        getAC()?.let {
            it.mCode = obj.data!!.code
            showCountdown()
            resumeTimer()
            smsStatus.text = "短信已下发到${it.phone}"
        }
    }

    private fun fetchFailure(){
        showRegainButton()
        getAC()?.let {
            smsStatus.text = "短信发送失败"
        }
    }

    private fun resumeTimer(){
        deInitTimer()
        mCount = 60
        if (mTimer == null){
            mTimer = Timer()
        }
        if (mTimerTask == null){
            mTimerTask = object :TimerTask() {
                override fun run() {
                    mCount -= 1
                    if (mCount <= 0){
                        deInitTimer()
                        showRegainButton()
                    }
                    configureCountdown()
                }
            }
        }
        mTimer!!.schedule(mTimerTask!!, Date(),1000)
    }

    private fun deInitTimer(){
        mTimer?.cancel()
        mTimer = null
        mTimerTask?.cancel()
        mTimerTask = null
    }

    private fun showRegainButton(){
        getAC()?.runOnUiThread {
            smsRegain.visibility = View.VISIBLE
            smsCountdown.visibility = View.INVISIBLE
        }
    }

    private fun showCountdown(){
        getAC()?.runOnUiThread {
            smsCountdown.visibility = View.VISIBLE
            smsRegain.visibility = View.INVISIBLE
        }
    }

    private fun configureCountdown(){
        getAC()?.runOnUiThread {
            smsCountdown.text = "${mCount}秒后重新获取验证码"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        deInitTimer()
    }

    private fun getAC() :CWRegisterActivity?{
        return activity as? CWRegisterActivity
    }



}
