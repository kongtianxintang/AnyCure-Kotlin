package com.example.chitwing.anycure_kotlin_master.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.chitwing.anycure_kotlin_master.R

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        val tag:String = "AnyCure-Kotlin"
    }

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         defaultSetting()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag,"销毁$this")
    }

    abstract fun initView()
    abstract fun fetchData()


    /**
     * 默认的设置
     * */
    private fun defaultSetting(){
        /**
         * 带有返回按钮
         * */
        val ac = supportActionBar
        ac?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(it.itemId){
                android.R.id.home -> onBackPressed()
                else -> return super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
