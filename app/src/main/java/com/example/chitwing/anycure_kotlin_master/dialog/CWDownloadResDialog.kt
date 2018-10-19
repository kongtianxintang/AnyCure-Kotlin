package com.example.chitwing.anycure_kotlin_master.dialog

import android.app.DialogFragment
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import com.github.ybq.android.spinkit.SpinKitView
import java.text.DecimalFormat

class CWDownloadResDialog: DialogFragment() {

    private var mProgressBar: ProgressBar? = null
    private var mProgressNum: TextView? = null
    private var mWaveView: SpinKitView? = null
    private var mRefresh: ImageButton? = null

    var callback: ResDialogInterface? = null

    interface ResDialogInterface {
        fun refreshAction()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(android.app.DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Material_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater?.inflate(R.layout.dialog_download_res,container)

        mProgressBar = v?.findViewById(R.id.progressBar)
        mProgressNum = v?.findViewById(R.id.progress_num)
        mWaveView = v?.findViewById(R.id.wave_view)
        mRefresh = v?.findViewById(R.id.mRetryButton)

        configureSubviews()

        return v!!
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val dm = DisplayMetrics()
            activity!!.windowManager!!.defaultDisplay.getMetrics(dm)
            it.window.setLayout((dm.widthPixels * 0.85).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    fun setProgressBarValue(arg: Int){
        mProgressBar?.progress = arg
        val format = DecimalFormat("00.00")
        format.positiveSuffix = "%"
        val target = "资源下载中..." + format.format(arg)
        mProgressNum?.text = target
    }

    /**
     * view都基本配置
     * */
    private fun configureSubviews(){
        loadingStatus()
        setProgressBarValue(0)
        mRefresh?.setOnClickListener {
            loadingStatus()
            callback?.refreshAction()
        }
    }

    /**
     * 失败状态
     * 展示一个刷新图标
     * */
    private fun failStatus(){
        mProgressBar?.visibility = View.INVISIBLE
        mProgressNum?.visibility = View.VISIBLE
        mWaveView?.visibility = View.INVISIBLE
        mRefresh?.visibility = View.VISIBLE
    }

    /**
     * 下载状态
     * */
    private fun loadingStatus(){
        mProgressBar?.visibility = View.VISIBLE
        mProgressNum?.visibility = View.VISIBLE
        mWaveView?.visibility = View.VISIBLE
        mRefresh?.visibility = View.INVISIBLE
    }

    /**
     * 显示错误信息
     * */
    fun failInfo(desc: String){
        failStatus()
        mProgressNum?.text = desc
    }

    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }
}