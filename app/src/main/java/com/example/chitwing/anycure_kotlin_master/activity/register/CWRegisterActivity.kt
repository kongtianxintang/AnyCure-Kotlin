package com.example.chitwing.anycure_kotlin_master.activity.register

import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.model.CWRegister
import com.example.chitwing.anycure_kotlin_master.model.ForgetBean
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import com.example.chitwing.anycure_kotlin_master.unit.CWRegex
import com.example.chitwing.anycure_kotlin_master.unit.showToast
import com.orhanobut.logger.Logger

import kotlinx.android.synthetic.main.activity_cwregister.*
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

enum class CWPasswordType {

    //注册,忘记密码
    Register,
    Forget;

    val desc by lazy {
        when(this){
            Register -> return@lazy "手机号注册"
            else -> return@lazy "忘记密码"
        }
    }
}
class CWRegisterActivity : BaseActivity() {

    /**
     * 类型 -
     * */
    var type: CWPasswordType = CWPasswordType.Register
    //输入手机号～
    private var mRegisterFragment:RegisterFragment? = null
    //验证码
    private var mSMSFragment:SMSFragment? = null
    //用户信息
    private var mUserInfoFragment:RegisterFragment? = null
    /**
     * 密码输入页
     * */
    private var mPasswordFragment: CWPasswordFragment? = null
    //服务器下发的验证码～
    var mCodes:String? = null
    //密码
    private var mPassWord:String? = null
    //性别
    private var mSex:String? = null
    //生日
    private var mBirthday:String? = null
    //手机号码
    var phone:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cwregister)
        initView()
        fetchData()
    }

    override fun initView() {
        //配置类型
        val tempType = intent.extras.get("type") as CWPasswordType
        this.type = tempType

        mRegisterFragment = RegisterFragment()
        mRegisterFragment!!.listener = mRegisterInterface
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.cw_register_content_view,mRegisterFragment!!)
        transaction.show(mRegisterFragment!!)
        transaction.commit()

        customTitle?.text = this.type.desc
    }

    override fun fetchData() {

    }

    private val mRegisterInterface = object :RegisterFragment.OnFragmentInteractionListener {

        override fun onFragmentLoginAction() {
            finish()
        }
        override fun onFragmentNextStep() {
            val tPhone = mRegisterFragment!!.registerPhone.text.toString()
            if (tPhone.isEmpty()){
                showToast("手机号码为空")
                return
            }
            val isRight = CWRegex.Phone.isRight(tPhone)
            if (isRight){
                phone = tPhone
                switchSMSFragment()
            }else{
                showToast("请输入正确的手机号")
            }

        }
    }

    private fun switchSMSFragment(){
        if (mSMSFragment == null){
            mSMSFragment = SMSFragment()
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.cw_register_content_view,mSMSFragment!!)
        transaction.show(mSMSFragment!!)
        transaction.hide(mRegisterFragment!!)
        transaction.commit()
    }

    fun switchPasswordFragment(){
        if (mPasswordFragment == null){
            mPasswordFragment = CWPasswordFragment()
            mPasswordFragment!!.listener = mPasswordCallback
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.cw_register_content_view,mPasswordFragment!!)
        transaction.show(mPasswordFragment!!)
        transaction.hide(mSMSFragment!!)
        transaction.commit()
    }

    private val mPasswordCallback = object :CWPasswordFragment.OnFragmentInteractionListener{
        override fun onFragmentInteraction(uri: Uri) {

        }
        override fun onNextStep(password: String) {
            mPassWord = password
            when (type) {
                CWPasswordType.Register -> registerAction()
                else -> forgetPasswordAction()
            }
        }
    }
    /**
     * 注册接口
     * */
    private fun registerAction(){
        if (phone != null && mPassWord != null){
            val birthdate = "2018-09-15"
            val city = "北京"
            //todo:获取imei 暂时不做 tm的要权限
//            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val imei = "imei"
            val sex = "man"
            val map = mapOf("mobile" to phone!!,"passwd" to mPassWord!!,"birthdate" to birthdate,"city" to city,"imei" to imei,"sex" to sex)
            val request = NetRequest.registerAction(map)
            request.enqueue(object :Callback<CWRegister>{
                override fun onFailure(call: Call<CWRegister>?, t: Throwable?) {
                    showToast("注册失败:${t.toString()}")
                }

                override fun onResponse(call: Call<CWRegister>?, response: Response<CWRegister>?) {
                    response?.body()?.let {
                        when (it.code) {
                            1000 -> {
                                showToast("注册成功")
                                finish()
                            }
                            else -> {
                                showToast("注册失败:${it.msg}")
                            }
                        }
                    }
                }
            })
        }
    }

    /**
     * 忘记密码
     * */
    private fun forgetPasswordAction(){
        val imei = "imei"
        val map = mapOf("mobile" to phone!!,"newPwd" to mPassWord!!,"imei" to imei)
        Logger.d("发送的数据->$map")
        val request = NetRequest.forgetPasswordAction(map)
        request.enqueue(object : Callback<ForgetBean> {
            override fun onFailure(call: Call<ForgetBean>, t: Throwable) {
                this@CWRegisterActivity.showToast(t.toString())
            }

            override fun onResponse(call: Call<ForgetBean>, response: Response<ForgetBean>) {
                val obj = response.body()
                obj?.let {
                    when(it.code) {
                        1000 -> {
                            this@CWRegisterActivity.showToast("修改密码成功")
                            finish()
                        }
                        else -> {
                            this@CWRegisterActivity.showToast(it.msg ?: "未知错误")
                        }
                    }
                }
            }
        })

    }


}
