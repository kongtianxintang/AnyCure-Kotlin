package com.example.chitwing.anycure_kotlin_master.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.R

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        val tag:String = "AnyCure-Kotlin"
    }

     override protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag,"销毁$this")
    }

    abstract fun initView()
    abstract fun fetchData()
}
