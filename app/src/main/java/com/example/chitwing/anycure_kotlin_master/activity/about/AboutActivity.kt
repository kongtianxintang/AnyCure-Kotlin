package com.example.chitwing.anycure_kotlin_master.activity.about

import android.os.Bundle
import android.webkit.WebSettings
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.network.NetRequest

import kotlinx.android.synthetic.main.content_about.*

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        initView()
    }

    override fun initView() {
        val path = NetRequest.CW_HOST_IP + "/about/about.html"
        about_web_view.loadUrl(path)

        /**
         * 对手机屏幕适应
         * */
        val setting = about_web_view.settings
        setting.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        setting.useWideViewPort = true
        setting.loadWithOverviewMode = true

    }

    override fun fetchData() {

    }



}
