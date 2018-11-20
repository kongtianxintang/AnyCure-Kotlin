package com.example.chitwing.anycure_kotlin_master.dialog

import android.app.DialogFragment
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.Constraints
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R
import java.text.DecimalFormat

class CWDownloadDialog: DialogFragment() {


    companion object {
        const val versionKey = "version"
        const val contentKey = "content"
    }

    interface DownloadDialogInterface {
        fun didClickButton(flag: Boolean)
    }

    private var mCloseButton: ImageButton? = null
    private var mUpdateButton: Button? = null
    private var mVersionTextView: TextView? = null
    private var mContentTextView: TextView? = null
    private var mTipsBg: ConstraintLayout? = null
    private var mProgressBar: ProgressBar? = null
    private var mProgressBg: ConstraintLayout? = null
    private var mProgressNum: TextView? = null

    private var mCallback: DownloadDialogInterface? = null

    fun setCallback(arg: DownloadDialogInterface?){
        mCallback = null
        mCallback = arg
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(android.app.DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Material_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater?.inflate(R.layout.dialog_download,container)

        mCloseButton = v?.findViewById(R.id.tipsCloseButton)
        mUpdateButton = v?.findViewById(R.id.updateButton)
        mVersionTextView = v?.findViewById(R.id.versionTextView)
        mContentTextView = v?.findViewById(R.id.tipsTextView)
        mProgressBar = v?.findViewById(R.id.progressBar)
        mTipsBg = v?.findViewById(R.id.tipsBg)
        mProgressBg = v?.findViewById(R.id.progressBg)
        mProgressNum = v?.findViewById(R.id.progress_num)


        configureSubviews()

        return v!!
    }

    private var mIsLayout = false

    override fun onStart() {
        super.onStart()
        dialog?.let {
            if (!mIsLayout){
                mIsLayout = true
                val dm = DisplayMetrics()
                activity!!.windowManager!!.defaultDisplay.getMetrics(dm)
                it.window.setLayout((dm.widthPixels * 0.85).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
    }

    private fun configureSubviews(){

        mCloseButton?.setOnClickListener {
            mCallback?.didClickButton(false)
            dismiss()
        }
        mUpdateButton?.setOnClickListener {
            mCallback?.didClickButton(true)
        }

        mProgressBar?.let {
            it.max = 100
        }

        val ver = arguments[versionKey] as? String
        val content = arguments[contentKey] as? String

        ver?.let {
            val new = "\n发现新版本!"
            val str = it + new

            mVersionTextView?.text = str
        }

        content?.let {
            val target = it.removeSuffix("\r\n")
            mContentTextView?.text = target
        }

        alertStatus()
        setProgressBarValues(0)
    }

    fun downloadStatus(){
        mTipsBg?.visibility = View.INVISIBLE
        mProgressBg?.visibility = View.VISIBLE
    }

    private fun alertStatus(){
        mTipsBg?.visibility = View.VISIBLE
        mProgressBg?.visibility = View.INVISIBLE
    }

    fun setProgressBarValues(arg: Int){
        mProgressBar?.progress = arg
        val format = DecimalFormat("00.00")
        format.positiveSuffix = "%"
        mProgressNum?.text = format.format(arg)
    }
}