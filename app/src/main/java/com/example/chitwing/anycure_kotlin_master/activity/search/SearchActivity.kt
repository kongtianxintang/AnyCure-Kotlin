package com.example.chitwing.anycure_kotlin_master.activity.search

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.model.BindDevice
import com.example.chitwing.anycure_kotlin_master.unit.showToast

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
//        fetchData()
        p()
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

    /**
     * 动态请求权限
     * */
    private fun p(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val check = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (check != PackageManager.PERMISSION_GRANTED){
                //todo:未授权
                Log.e(tag,"未授权")
                val list = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
                this.requestPermissions(list,0x01)
            }else{
                fetchData()
            }
        }else{
            fetchData()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e(tag,"code->$requestCode")
        if (requestCode == 0x01){
            val g = grantResults[0]
            if (g == PackageManager.PERMISSION_GRANTED){
                fetchData()
            }else{
                //todo:
                showToast("使用蓝牙需要定位权限")
            }
        }
    }
}
