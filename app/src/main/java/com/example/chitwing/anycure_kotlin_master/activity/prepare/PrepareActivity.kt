package com.example.chitwing.anycure_kotlin_master.activity.prepare

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.WindowDecorActionBar
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.util.Log
import android.view.View
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

    private val deviceProvider by lazy {
        return@lazy PrepareStatusProvider(this)
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
    }


    override fun initView() {
        titleTextView = findViewById(R.id.prepare_title)
        startButton = findViewById(R.id.prepare_button)
        tabLayout = findViewById(R.id.prepare_tab_layout)
        content = findViewById(R.id.prepare_content)
        content.movementMethod = ScrollingMovementMethod.getInstance()

        startButton.setOnClickListener {
            deviceProvider.electrodeQuery()
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
        deviceProvider.beginWriteData()
    }

    /**
     * 销毁的时候 删除最新链接的一个外设
     * */
    override fun onBackPressed() {
        super.onBackPressed()
        /**
        * finish 先于 onBackPressed 执行
        * */
        if (CWBleManager.mCWDevices.isNotEmpty()){
            val last = CWBleManager.mCWDevices.last()
            last.removeSelf()
        }
    }

    override fun finish() {
        super.finish()
        deviceProvider.mDevice = null
    }
}
