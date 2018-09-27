package com.example.chitwing.anycure_kotlin_master.activity.search

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.unit.Unit
import com.example.chitwing.anycure_kotlin_master.unit.showToast
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_search.*

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
        startScanPrepare()
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.search_recycler_view)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        mAdapter = CWSearchAdapter(mDataSet,this)
        mRecyclerView!!.adapter = mAdapter!!
        searchStatus()
        configureSearch()

        mReSearch.setOnClickListener {
            startScanPrepare()
        }
        customTitle?.text = getText(R.string.search_title)
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
    private fun startScanPrepare(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val check = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (check != PackageManager.PERMISSION_GRANTED){
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
        Logger.e("code->$requestCode")
        if (requestCode == 0x01){
            val g = grantResults[0]
            if (g == PackageManager.PERMISSION_GRANTED){
                fetchData()
            }else{
                showToast("使用蓝牙需要定位权限")
            }
        }
    }

    /**
     * 搜索状态
     * */
    fun searchStatus(){
        runOnUiThread {
            mReSearch.visibility = View.INVISIBLE
            mSearchDevice.visibility = View.VISIBLE
            mSearchPhone.visibility = View.VISIBLE
            mSearchLoading.visibility = View.VISIBLE
            configureSearch()
        }
    }

    /**
     * 暂时结果状态
     * */
    fun resultStatus(){
        runOnUiThread {
            mReSearch.visibility = View.VISIBLE
            mSearchDevice.visibility = View.INVISIBLE
            mSearchPhone.visibility = View.INVISIBLE
            mSearchLoading.visibility = View.INVISIBLE
            configureResult()
        }
    }

    /**
     * 富文本 显示搜索结果 or 显示正在搜索
     * */
    private fun configureSearch(){
        val t = "请打开设备\n"
        val d = "正在搜索设备..."
        val color = ForegroundColorSpan(Color.WHITE)
        val size = AbsoluteSizeSpan(Unit.dip2px(20f))

        val spanBuilder = SpannableStringBuilder(t)

        spanBuilder.setSpan(size,0,t.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanBuilder.setSpan(color,0,t.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spanBuilder.append(d)


        val smallColor = ForegroundColorSpan(Color.WHITE)
        val smallSize = AbsoluteSizeSpan(Unit.size2sp(12f,this).toInt())
        spanBuilder.setSpan(smallColor,t.length,spanBuilder.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanBuilder.setSpan(smallSize,t.length,spanBuilder.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mSearchText.text = spanBuilder

    }

    private fun configureResult(){
        val num = mDataSet.count()
        val t = "扫描完成\n"
        val d = "发现可用设备${num}个"
        val color = ForegroundColorSpan(Color.WHITE)
        val size = AbsoluteSizeSpan(Unit.dip2px(20f))

        val spanBuilder = SpannableStringBuilder(t)

        spanBuilder.setSpan(size,0,t.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanBuilder.setSpan(color,0,t.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spanBuilder.append(d)


        val smallColor = ForegroundColorSpan(Color.WHITE)
        val smallSize = AbsoluteSizeSpan(Unit.size2sp(12f,this).toInt())
        spanBuilder.setSpan(smallColor,t.length,spanBuilder.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanBuilder.setSpan(smallSize,t.length,spanBuilder.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mSearchText.text = spanBuilder
    }
}
