package com.example.chitwing.anycure_kotlin_master.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.app.MyApp
import com.example.chitwing.anycure_kotlin_master.unit.Unit
import com.orhanobut.logger.Logger

abstract class BaseActivity : AppCompatActivity() {

    var customTitle:TextView? = null
    var customLeftBar: ImageButton? = null


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
        val lp = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT,Gravity.CENTER)
        val actionView = LayoutInflater.from(this).inflate(R.layout.action_bar_layout,null)
        supportActionBar?.let {
            it.setCustomView(actionView,lp)
            it.setDisplayShowCustomEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(false)
        }
        customLeftBar = actionView.findViewById(R.id.mLeftImageButton)
        customTitle = actionView.findViewById(R.id.mActionBarTextView)
        customLeftBar?.setOnClickListener {
            onBackPressed()
        }

        /**
        * 自适应
        * */


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

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("销毁")
    }
}
