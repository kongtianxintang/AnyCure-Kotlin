package com.example.chitwing.anycure_kotlin_master.activity.register

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity

import kotlinx.android.synthetic.main.activity_cwregister.*

class CWRegisterActivity : BaseActivity() {

    //输入手机号～
    private var mRegisterFragment:RegisterFragment? = null
    //验证码
    private var mSMSFragment:SMSFragment? = null
    //用户信息
    private var mUserInfoFragment:RegisterFragment? = null
    //服务器下发的验证码～
    private var mCode:Int = 0
    // let body = ["mobile":phone,"passwd":pw,"sex":sexStr,"birthdate":dateStr,"imei":imei,"city":city];
    //密码
    private var mPassWord:String? = null
    //性别
    private var mSex:String? = null
    //生日
    private var mBirthday:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cwregister)
        initView()
        fetchData()
    }

    override fun initView() {
        val type = intent.extras.getInt("type")
        mRegisterFragment = RegisterFragment.newInstance(type)
        mRegisterFragment!!.listener = mRegisterInterface
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.cw_register_content_view,mRegisterFragment!!)
        transaction.show(mRegisterFragment!!)
        transaction.commit()
    }

    override fun fetchData() {

    }

     private val mRegisterInterface = object :RegisterFragment.OnFragmentInteractionListener {

        override fun onFragmentLoginAction() {
            finish()
        }
        override fun onFragmentNextStep() {
            switchSMSFragment()
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

}
