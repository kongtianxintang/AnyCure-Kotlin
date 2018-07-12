package com.example.chitwing.anycure_kotlin_master.fragment.cure


import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.ui.CWProgressView


/**
 * 理疗页面
 */
class CureFragment : BaseFragment() {


    private lateinit var mProgress:CWProgressView
    private lateinit var mAddButton:AppCompatButton
    private lateinit var mMinusButton:AppCompatButton
    private lateinit var mIntensityText:TextView
    /**
     * 数据处理
     * */
    private val provider by lazy {
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

        buttonAction()
    }

    /**
     * button 点击事件
     * */
    private var intentsy = 0
    private fun buttonAction(){
        mAddButton.setOnClickListener {
            if (intentsy >= 50){
                return@setOnClickListener
            }
            intentsy++
            mProgress.setCurrent(intentsy)
            mIntensityText.text = "$intentsy"
        }

        mMinusButton.setOnClickListener {
            if (intentsy <= 0){
                return@setOnClickListener
            }
            intentsy--
            mProgress.setCurrent(intentsy)
            mIntensityText.text = "$intentsy"
        }
    }

}
