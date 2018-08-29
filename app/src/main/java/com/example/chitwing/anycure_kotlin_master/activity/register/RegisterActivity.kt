package com.example.chitwing.anycure_kotlin_master.activity.register

import android.os.Bundle
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity

import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_register.*

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
        fetchData()
    }

    override fun initView() {
        register_button.setOnClickListener {
            finish()
        }
    }

    override fun fetchData() {

    }

}
