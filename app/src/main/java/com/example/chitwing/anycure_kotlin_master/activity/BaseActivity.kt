package com.example.chitwing.anycure_kotlin_master.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.chitwing.anycure_kotlin_master.app.MyApp
import com.example.chitwing.anycure_kotlin_master.unit.Unit

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        val tag:String = "AnyCure-Kotlin"
    }

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         defaultSetting()
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /**
        * 自适应
        * */
        Unit.customDensity(this,MyApp.getApp())

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
