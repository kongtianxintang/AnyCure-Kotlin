package com.example.chitwing.anycure_kotlin_master.activity.register

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.unit.CWRegex
import com.example.chitwing.anycure_kotlin_master.unit.showToast
import kotlinx.android.synthetic.main.fragment_cwpassword.*


/**
 * 密码输入
 */
class CWPasswordFragment : BaseFragment() {

    var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cwpassword, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        defaultSubviews()
    }

    private fun defaultSubviews(){
        mRegister.setOnClickListener {
            val tps = checkPassword()
            tps?.let {
                listener?.onNextStep(it)
            }
        }
        fetchData()
    }




    /**
     * 确认密码
     * */
    private fun checkPassword(): String?{
        val tps = mPassword.text.toString()
        if (tps.isEmpty()){
            activity?.showToast("请输入密码")
            return null
        }
        val isRight = CWRegex.Password.isRight(tps)
        if (isRight){
            return tps
        }
        activity?.showToast("请输入正确的密码")
        return null
    }

    private fun fetchData(){
        val type = (activity as CWRegisterActivity).type
        val str = when (type) {
            CWPasswordType.Register -> "注册"
            else -> "重置密码"
        }
        mRegister.text = str
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
        fun onNextStep(password: String)
    }

}
