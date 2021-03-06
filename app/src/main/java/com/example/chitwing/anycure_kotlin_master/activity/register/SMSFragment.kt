package com.example.chitwing.anycure_kotlin_master.activity.register


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.model.SMSCode
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import com.orhanobut.logger.Logger
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

    /**
     * 倒计时
     * */
    private var mTimer:Timer? = null
    private var mTimerTask:TimerTask? = null
    private var mCount = 0

    /**
     * 子控件
     * */
    private var mSmsStatus: TextView? = null
    private var mSmsCountdown: TextView? = null
    private var mSmsRegain: Button? = null
    private var mSmsNum: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sm, container, false)

        mSmsStatus = view.findViewById(R.id.smsStatus)
        mSmsCountdown = view.findViewById(R.id.smsCountdown)
        mSmsRegain = view.findViewById(R.id.smsRegain)
        mSmsNum = view.findViewById(R.id.smsNum)

        return view
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
        mSmsCountdown?.visibility = View.INVISIBLE
        mSmsRegain?.visibility = View.INVISIBLE

        mSmsRegain?.setOnClickListener {
            fetchCode()
        }

        mSmsNum?.addTextChangedListener( object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.length >= 6){
                        val tCode = it.toString()
                        Logger.d("用户输入的验证码$tCode")
                        val ac = getAC()
                        if (tCode == ac?.mCodes){
                            //切换到密码页面
                            ac.switchPasswordFragment()
                        }
                    }
                }
            }
        })
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
        map["channel"] = CWBleManager.configure.channel.NUM_CODE
        val call = NetRequest.fetchSMSCode(map)
        call.enqueue( object :Callback<SMSCode> {
            override fun onResponse(call: Call<SMSCode>?, response: Response<SMSCode>?) {
                response?.body()?.let {
                    when(it.code){
                        0 -> {
                            Logger.d("验证码->${it.data?.code}")
                            fetchSuccess(it)
                        }
                        1 -> {
                            Logger.d("错误信息->${it.msg} map->$map")
                            fetchFailure(it.msg)
                        }
                        else -> {
                            fetchFailure(it.msg)
                        }
                    }
                }

            }

            override fun onFailure(call: Call<SMSCode>?, t: Throwable?) {
                fetchFailure(t.toString())
            }
        })

    }

    private fun defaultSMSStatus(){
        getAC()?.let {
            mSmsStatus?.text = "正在发送短信到${it.phone}"
        }
    }
    /**
     * 获取成功
     * */
    private fun fetchSuccess(obj:SMSCode){
        getAC()?.let {
            it.mCodes = obj.data!!.code
            showCountdown()
            resumeTimer()
            mSmsStatus?.text = "短信已下发到${it.phone}"
        }
    }

    private fun fetchFailure(error: String?){
        showRegainButton()
        getAC()?.let {
            mSmsStatus?.text = "短信发送失败:$error"
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
            mSmsRegain?.visibility = View.VISIBLE
            mSmsCountdown?.visibility = View.INVISIBLE
        }
    }

    private fun showCountdown(){
        getAC()?.runOnUiThread {
            mSmsCountdown?.visibility = View.VISIBLE
            mSmsRegain?.visibility = View.INVISIBLE
        }
    }

    private fun configureCountdown(){
        getAC()?.runOnUiThread {
            mSmsCountdown?.text = "${mCount}秒后重新获取验证码"
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
