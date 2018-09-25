package com.example.chitwing.anycure_kotlin_master.activity.bind

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageButton
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.activity.search.SearchActivity
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.model.BindDevice

class BindActivity : BaseActivity() {

    /**
     * wo
     * */
    private val mRequestCode = 0x01
    /**
     * 数据源
     * */
    var mDataSet = mutableListOf<BindDevice>()

    /**
     * 添加按钮
     * */
    private var mAddButton:ImageButton? = null

    /**
     * RecyclerView
     * */
    private var mRecyclerView:RecyclerView? = null

    /**
     * 适配器
     * */
    var mAdapter:CWBindAdapter? = null

    /**
     * 数据or逻辑处理类
     * */
    private val mProvider by lazy {
        return@lazy CWBindProvider(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind)
        initView()
        fetchData()
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.bind_recyclerView)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        mAddButton = findViewById(R.id.bind_add_device)
        mAddButton!!.setOnClickListener {
            startSearchActivity()
        }
        customTitle?.text = getText(R.string.bind_title)
    }

    override fun fetchData() {
        mAdapter =  CWBindAdapter(mDataSet,this)
        mRecyclerView!!.adapter =  mAdapter!!
        mProvider.fetchDataSource()
    }

    /**
     * 去搜索设备页面
     * */
    private fun startSearchActivity(){
        val intent = Intent(this,SearchActivity ::class.java)
        startActivityForResult(intent,mRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mRequestCode){
            mProvider.fetchDataSource()
        }
    }

}
