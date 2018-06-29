package com.example.chitwing.anycure_kotlin_master.activity.prepare

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.WindowDecorActionBar
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager

/**
 * 理疗准备页面
 * */
class PrepareActivity : BaseActivity() {

    private val provider by lazy {
        return@lazy PrepareProvider(this)
    }

    lateinit var titleTextView:TextView
    lateinit var startButton:Button
    lateinit var tabLayout:TabLayout
    lateinit var content:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepare)
        initView()
        fetchData()
        Log.e(tag,"onCreate")
    }

    override fun initView() {
        titleTextView = findViewById(R.id.prepare_title)
        startButton = findViewById(R.id.prepare_button)
        tabLayout = findViewById(R.id.prepare_tab_layout)
        content = findViewById(R.id.prepare_content)
        content.movementMethod = ScrollingMovementMethod.getInstance()

        startButton.setOnClickListener {
            //todo:开始理疗

            //todo:销毁此activity
            finish()
        }

        tabLayout.addOnTabSelectedListener(tabListener)

    }

    private val tabListener = object :TabLayout.OnTabSelectedListener{
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let {
                provider.switchTab(it.position)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }
    }

    override fun fetchData() {
        provider.configureView()
    }

    /**
     * 销毁的时候 删除最新链接的一个外设
     * */
    override fun finish() {
        super.finish()
        val last = CWBleManager.mCWDevices.last()
        last?.let {
            it.removeSelf()
        }
    }
}
