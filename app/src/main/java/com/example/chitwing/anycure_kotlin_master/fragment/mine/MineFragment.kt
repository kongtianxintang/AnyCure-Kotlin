package com.example.chitwing.anycure_kotlin_master.fragment.mine

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.LoginActivity
import com.example.chitwing.anycure_kotlin_master.activity.about.AboutActivity
import com.example.chitwing.anycure_kotlin_master.activity.about.CWAboutActivity
import com.example.chitwing.anycure_kotlin_master.activity.bind.BindActivity
import com.example.chitwing.anycure_kotlin_master.activity.shared.SharedActivity
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.dialog.CWDialog
import com.example.chitwing.anycure_kotlin_master.dialog.CWDialogInterface
import com.example.chitwing.anycure_kotlin_master.download.DownloadConfigure
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.model.Login
import com.example.chitwing.anycure_kotlin_master.model.MineModel
import com.example.chitwing.anycure_kotlin_master.unit.*
import com.example.chitwing.anycure_kotlin_master.unit.Unit
import com.orhanobut.logger.Logger
import java.io.File

/**
 * 个人页面
 * */
class MineFragment : BaseFragment() {

    private lateinit var mRecyclerView:RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_mine, container, false)

        mRecyclerView = v.findViewById(R.id.mine_recycler_view)

        defaultConfigureView()

        return v
    }


    private fun defaultConfigureView(){

        val device = MineModel("","绑定设备")
        val help = MineModel("","帮助")
        val about = MineModel("","关于我们")
        val share = MineModel("","分享软件")

        val data = listOf(device,help,about,share)
        val mad = OtMineAdapter(data,R.layout.mine_item)

        val subs = listOf(BindActivity ::class.java,null,CWAboutActivity ::class.java,SharedActivity ::class.java)

        mRecyclerView.adapter = mad
        mad.setOnItemClickListener { _, _, position ->
            val obj = subs[position]
            if (obj == null) {
                activity?.showToast("功能未开放哦")
            }else{
                val intent = Intent(this.activity!!,obj)
                startActivity(intent)
            }

        }

        mRecyclerView.layoutManager = LinearLayoutManager(this.context!!, LinearLayoutManager.VERTICAL,false)

        val header = getHeaderView()
        val icon = header.findViewById<ImageView>(R.id.mine_header_icon)
        val telephone = header.findViewById<TextView>(R.id.mineTelephone)
        val logs = DBHelper.findAll(Login ::class.java)
        val log = logs?.lastOrNull()
        log?.let {
            icon.loadCircleRemoteUrl(this.activity!!,it.icon)
        }
        val phone = SharedPreferencesHelper.getObject(SharedPreferencesHelper.telephone,"134 **** 2934") as String
        val temp = phone.replaceRange(3 until 7,"****")
        telephone.text = temp

        mad.addHeaderView(header)
        val footer = getFooterView()
        mad.addFooterView(footer)

    }


    private fun getHeaderView(): View {
        return layoutInflater.inflate(R.layout.mine_header_view, mRecyclerView.parent as ViewGroup, false)
    }

    private fun getFooterView(): View {
        val footer = layoutInflater.inflate(R.layout.mine_footer_view, mRecyclerView.parent as ViewGroup, false)
        val button:Button = footer.findViewById(R.id.mine_footer_log_out_button)
        button.setOnClickListener {
            val dialog = CWDialog.Builder().setDesc("你确定要退出吗?").setTitle("退出登录").create()
            dialog.setCallback( object :CWDialogInterface {
                override fun onClickButton(flag: Boolean, item: CWDialog) {
                    item.dismiss()
                    if (flag){//退出登录
                        userLogOut()
                    }
                }
            })
            dialog.show(activity!!.fragmentManager,"logOut")
        }
        return footer
    }

    private fun userLogOut(){
        Logger.d("删除登录信息 还有那个地方？")
        DBHelper.removeAll(Login ::class.java)
        CWBleManager.mCWDevices.forEach { it.endCureAction() }
        val intent = Intent(activity,LoginActivity ::class.java)
        startActivity(intent)
        activity?.finish()
    }
}
