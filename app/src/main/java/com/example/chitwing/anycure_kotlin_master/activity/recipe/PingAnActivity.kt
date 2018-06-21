package com.example.chitwing.anycure_kotlin_master.activity.recipe

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.base.CWOnItemClickListener
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.model.Recipe

class PingAnActivity : BaseActivity() {

    lateinit var mRecyclerView:RecyclerView

    private lateinit var layoutManager: RecyclerView.LayoutManager

    private var provider:PingAnProvider = PingAnProvider(this)

    lateinit var mRefresh:SwipeRefreshLayout

    var mAdapter:PingAnAdapter? = null

    /**
     * 数据源
     * */
    var mDataSet:MutableList<Recipe> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ping_an)
        initView()
        fetchData()
    }

    override fun initView() {

        mRefresh = findViewById(R.id.recipe_ping_an_refresh)
        mRefresh.setSize(SwipeRefreshLayout.LARGE)
        mRefresh.setColorSchemeResources(R.color.colorPrimary,R.color.error_color_material)

        mRefresh.setOnRefreshListener {
            Log.d(tag,"开始刷新")
            fetchData()
        }

        mRecyclerView = findViewById(R.id.recipe_recyclerView)
        layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        mRecyclerView.layoutManager = layoutManager

        val data =  DBHelper.findAll(this,Recipe ::class.java)

        data?.let {
            Log.d(tag,"保存在本地的数据个数:${it.count()}")
            mDataSet.addAll(it)
        }

        mAdapter = PingAnAdapter(mDataSet,this)

        mAdapter!!.onItemClickListenner = object : CWOnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.e(tag,"点击事件 view->$view,\n位置->$position")
            }
        }

        mRecyclerView.adapter = mAdapter

    }

    override fun fetchData() {
        provider.fetchDataSource()
    }


}
