package com.example.chitwing.anycure_kotlin_master.activity.search

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.model.BindDevice

class SearchActivity : BaseActivity() {

    /**
     * 逻辑处理类
     * */
    val mProvider by lazy {
        return@lazy CWSearchProvider(this)
    }

    /**
     * 适配器
     * */
    var mAdapter:CWSearchAdapter? = null

    /**
     * RecyclerView
     * */
    private var mRecyclerView :RecyclerView? = null
    /**
     * 数据源
     * */
    var mDataSet = mutableListOf<BluetoothDevice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initView()
        fetchData()
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.search_recycler_view)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        mAdapter = CWSearchAdapter(mDataSet,this)
        mRecyclerView!!.adapter = mAdapter!!
    }

    override fun fetchData() {
        mProvider.fetchDataSource()
    }

    override fun finish() {
        mProvider.finish()
        super.finish()
    }
}
