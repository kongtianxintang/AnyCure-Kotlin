package com.example.chitwing.anycure_kotlin_master.activity.about

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.BuildConfig
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.dialog.CWDownloadDialog
import com.example.chitwing.anycure_kotlin_master.download.DownloadAPKProvider
import com.example.chitwing.anycure_kotlin_master.download.DownloadFileTask
import com.example.chitwing.anycure_kotlin_master.model.AboutBean
import com.example.chitwing.anycure_kotlin_master.model.Version
import com.example.chitwing.anycure_kotlin_master.unit.showToast
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_cwabout.*
import java.io.File

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/10/23
 * QQ/Tel/Mail:383118832
 * Description:
 * Others:新手勿喷
 * Modifier:
 * Reason:
 *************************************************************/

class CWAboutActivity : BaseActivity() {

    private val mDataSet by lazy {
        return@lazy mutableListOf<AboutBean>()
    }
    private lateinit var mAdapter: CWAboutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cwabout)
        initView()
        fetchData()
    }

    override fun fetchData() {
        configureData()
        configureSubviews()
    }

    override fun initView() {
        customTitle?.text = getText(R.string.title_activity_about)

        Logger.d("啊哈哈->$mRecyclerView")
    }

    /**
     * 配置数据
     * */
    private fun configureData(){
        //本地版本
        val location = AboutBean("当前版本", BuildConfig.VERSION_NAME,false)
        mDataSet.add(location)
        //服务器版本
        val versions = DBHelper.findAll(Version ::class.java)
        val last = versions?.lastOrNull()
        if (last != null){
            Logger.d("版本信息 code->${last.code} version->${last.version} ")
            when(last.code){

                1 -> {//有新版本
                    last.version?.let {
                        val str = "有新版本:" + it
                        val remote = AboutBean("新版本更新",str,true)
                        mDataSet.add(remote)
                    }
                }
                else -> {//无新版本
                    val remote = AboutBean("新版本更新","已经是最新版本",false)
                    mDataSet.add(remote)
                }
            }
        }else{
            val remote = AboutBean("新版本更新", "已经是最新版本",false)
            mDataSet.add(remote)
        }
    }

    /**
     * 配置子控件
     * */
    private fun configureSubviews(){
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        mAdapter = CWAboutAdapter(mDataSet,R.layout.item_about)
        mRecyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->
            onItemClick(position)
        }

        val header = getHeaderView()
        mAdapter.addHeaderView(header)

        val footer = getFooterView()
        mAdapter.addFooterView(footer)

    }

    private fun getHeaderView(): View {
        return layoutInflater.inflate(R.layout.about_header_view, mRecyclerView.parent as ViewGroup, false)
    }

    private fun getFooterView(): View {
        val footer = layoutInflater.inflate(R.layout.about_footer_view, mRecyclerView.parent as ViewGroup, false)
        val copyright = footer.findViewById<TextView>(R.id.mCopyright)
        copyright.text = CWBleManager.configure.channel.copyright
        return footer
    }

    /**
     * 点击事件
     * */
    private fun onItemClick(index: Int){
        when(index){
            1 -> {
                val versions = DBHelper.findAll(Version ::class.java)
                val last = versions?.lastOrNull()
                last?.let {
                    when(it.code){
                        1 -> { pushDownloadDialog(it.url!!,it.content!!,it.version!!) }
                        else -> { showToast("已是最新版本") }
                    }
                }
            }
            else -> {

            }
        }
    }

    private fun pushDownloadDialog(url: String,desc:String,ver: String){
        val dialog = CWDownloadDialog()
        val bundle = Bundle()
        bundle.putString(CWDownloadDialog.versionKey,ver)
        bundle.putString(CWDownloadDialog.contentKey,desc)
        dialog.arguments = bundle
        dialog.show(fragmentManager,"download")
        dialog.setCallback(object : CWDownloadDialog.DownloadDialogInterface {
            override fun didClickButton(flag: Boolean) {
                if (flag){
                    dialog.downloadStatus()

                    val callback = object : DownloadFileTask.DownloadFileInterface {
                        override fun downloadProgress(arg: Int) {
                            dialog.setProgressBarValues(arg)
                        }

                        override fun downloadSuccessful(flag: Boolean) {
                            dialog.dismiss()
                            val str = if (flag) "下载成功" else "下载失败"
                            this@CWAboutActivity.showToast(str)
                            if (flag){
                                installApk()
                            }
                        }
                    }
                    //去下载
                    val task = DownloadAPKProvider(this@CWAboutActivity,callback = callback)
                    task.downloadTask(url)
                }
            }
        })
    }

    /**
     * 安装apk
     * */
    private fun installApk(){
        val path = getExternalFilesDir(null)
        val file = File(path,"may_able.apk")
        if (file.exists()){
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val name = BuildConfig.APPLICATION_ID + ".fileProvider"
                val pack = FileProvider.getUriForFile(this,name,file)
                intent.setDataAndType(pack, "application/vnd.android.package-archive")
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            }
            startActivity(intent)
        }
    }
}
