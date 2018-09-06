package com.example.chitwing.anycure_kotlin_master.fragment.mine

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.model.Login
import com.example.chitwing.anycure_kotlin_master.model.MineModel
import com.example.chitwing.anycure_kotlin_master.unit.Unit
import com.example.chitwing.anycure_kotlin_master.unit.loadRadius
import com.example.chitwing.anycure_kotlin_master.unit.loader

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

        val data = listOf(device,help,about,share)
        val mad = OtMineAdapter(data,R.layout.mine_item)

//        val list = listOf(device,help,about,share)
//        mAdapter = MineAdapter(list,this)

        mRecyclerView.adapter = mad

        mRecyclerView.layoutManager = LinearLayoutManager(this.context!!, LinearLayoutManager.VERTICAL,false)

        val header = getHeaderView()
        val icon = header.findViewById<ImageView>(R.id.mine_header_icon)
        val logs = DBHelper.findAll(Login ::class.java)
        val log = logs?.lastOrNull()
        log?.let {
            val px = Unit.Dp2Px(activity!!,160)
            icon.loadRadius(this.activity!!,it.icon,px)
            Log.d("个人中心","用户头像${it.icon} px->$px")
        }


        mad.addHeaderView(header)
        val footer = getFooterView()
        mad.addFooterView(footer)

    }


    private fun getHeaderView(): View {
        return layoutInflater.inflate(R.layout.mine_header_view, mRecyclerView.parent as ViewGroup, false)
    }

    private fun getFooterView(): View {
        return layoutInflater.inflate(R.layout.mine_footer_view, mRecyclerView.parent as ViewGroup, false)
    }
}
