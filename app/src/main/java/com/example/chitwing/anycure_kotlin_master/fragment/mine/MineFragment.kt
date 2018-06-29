package com.example.chitwing.anycure_kotlin_master.fragment.mine

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.model.MineModel

/**
 * 个人页面
 * */
class MineFragment : BaseFragment() {


    private lateinit var mRecyclerView:RecyclerView
    private lateinit var mAdapter:MineAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_mine, container, false)

        mRecyclerView = v.findViewById(R.id.mine_recycler_view)

        defaultConfigureView()

        return v
    }


    private fun defaultConfigureView(){

        val device = MineModel("","我的设备")
        val help = MineModel("","帮助与反馈")
        val about = MineModel("","关于我们")
        val share = MineModel("","分享软件")

        val list = listOf(device,help,about,share)

        mAdapter = MineAdapter(list,this)

        mRecyclerView.adapter = mAdapter

        mRecyclerView.layoutManager = LinearLayoutManager(this.context!!, LinearLayoutManager.VERTICAL,false)

    }

}
