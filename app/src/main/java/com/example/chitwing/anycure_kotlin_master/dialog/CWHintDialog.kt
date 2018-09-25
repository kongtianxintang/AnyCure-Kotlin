package com.example.chitwing.anycure_kotlin_master.dialog

import android.os.Bundle
import android.app.DialogFragment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.unit.SharedPreferencesHelper

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/9/20
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/
class CWHintDialog: DialogFragment() {

    private var mButton: Button? = null
    private var mRadioButton: RadioButton? = null
    private var mHintDesc: TextView? = null
    private var mHintTitle: TextView? = null
    private var mIgnore: Boolean = false//是否下次忽略

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(android.app.DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Dialog_MinWidth)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater?.inflate(R.layout.dialog_hint,container)
        mButton = v?.findViewById(R.id.mButton)
        mRadioButton = v?.findViewById(R.id.mRadio)
        mHintDesc = v?.findViewById(R.id.mHint)
        mHintTitle = v?.findViewById(R.id.mHintTitle)
        configureSubviews()
        return v!!
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val dm = DisplayMetrics()
            activity!!.windowManager!!.defaultDisplay.getMetrics(dm)
            it.window.setLayout((dm.widthPixels * 0.85).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    private fun configureSubviews(){
        mButton?.setOnClickListener {
            dismiss()
            if (mIgnore){
                SharedPreferencesHelper.put(SharedPreferencesHelper.hintKey,true)
            }
        }
        mRadioButton?.setOnCheckedChangeListener { _, isChecked ->
            mIgnore = isChecked
        }

    }

}