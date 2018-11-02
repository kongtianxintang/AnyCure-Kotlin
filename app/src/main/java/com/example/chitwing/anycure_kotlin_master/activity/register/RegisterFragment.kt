package com.example.chitwing.anycure_kotlin_master.activity.register

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.unit.Unit
import kotlinx.android.synthetic.main.fragment_register.*

private const val ARG_TYPE = "ARG_TYPE"

/**
 *
 */
class RegisterFragment : BaseFragment() {
    private var param1: Int? = null
    var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_TYPE)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val type = (activity as? CWRegisterActivity)?.type
        initView()
        fetchData(type)
    }

    private fun initView() {
        registerNextStep.setOnClickListener {
            onButtonPressed()
        }
        registerHasAccount.setOnClickListener {
            listener?.onFragmentLoginAction()
        }
    }

    private fun onButtonPressed() {
        listener?.onFragmentNextStep()
    }

    private fun fetchData(type:CWPasswordType?) {
        type?.let {
            when(it){
                CWPasswordType.Register -> {
                    configureHasAccount()
                }
                CWPasswordType.Forget -> {
                    hiddenHasAccount()
                }
            }
        }
    }


    private fun hiddenHasAccount(){
        registerHasAccount.visibility = View.GONE
    }

    private fun configureHasAccount(){
        val account = "已有账号 "
        val login = "登录"
        val color = ForegroundColorSpan(ContextCompat.getColor(this.activity!!,R.color.app_gray))
        val size = AbsoluteSizeSpan(Unit.dip2px(15f))

        val spanBuilder = SpannableStringBuilder(account)

        spanBuilder.setSpan(size,0,account.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanBuilder.setSpan(color,0,account.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spanBuilder.append(login)

        val normal = ForegroundColorSpan(ContextCompat.getColor(this.activity!!,R.color.main))
        val normalSize = AbsoluteSizeSpan(Unit.dip2px(17f))
        spanBuilder.setSpan(normal,account.length,spanBuilder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanBuilder.setSpan(normalSize,account.length,spanBuilder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        registerHasAccount.text = spanBuilder
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentNextStep()
        fun onFragmentLoginAction()
    }
}
