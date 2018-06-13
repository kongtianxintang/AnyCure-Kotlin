package com.example.chitwing.anycure_kotlin_master.activity.recipe

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity

class PingAnActivity : BaseActivity() {

    lateinit var mRecyclerView:RecyclerView

    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ping_an)
        initView()
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.recipe_recyclerView)
        layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = PingAnAdapter(dataSet)
    }

    override fun fetchData() {

    }

    private val dataSet:List<String>  = List(50,{i -> "位置哦$i"})

}
