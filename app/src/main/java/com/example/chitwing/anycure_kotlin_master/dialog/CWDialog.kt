package com.example.chitwing.anycure_kotlin_master.dialog

import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.R

/***********************************************************
 * 版权所有,2018,Chitwing.
 * Copyright(C),2018,Chitwing co. LTD.All rights reserved.
 * project:AnyCure-Kotlin
 * Author:chitwing
 * Date:  2018/7/30
 * QQ/Tel/Mail:383118832
 * Description:dialog
 * Others:新手勿喷 后期加入type 根据类型的不同加载不一样的视图
 * Modifier:
 * Reason:
 *************************************************************/

enum class CWDialogType{
    Error,//错误
    Hint,//提示
    Other//其他
}

class CWDialog :DialogFragment(){

    private var mTitle:TextView? = null
    private var mDesc:TextView? = null
    private var mCancel:Button? = null
    private var mSure:Button? = null
    private var mIcon:ImageView? = null
    private var mAlone:Button? = null
    private var mCallback:CWDialogInterface? = null

    fun setCallback(callback:CWDialogInterface){
        this.mCallback = callback
    }
    private var mBuilder:CWDialog.Builder? = null

    fun getType():CWDialogType{
        if (mBuilder != null){
            return mBuilder!!.getType()
        }else{
            throw NullPointerException()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Dialog_MinWidth)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater?.inflate(R.layout.fragment_cw_dialog,container)

        mTitle = v!!.findViewById(R.id.cw_dialog_title)
        mDesc = v.findViewById(R.id.cw_dialog_desc)
        mCancel = v.findViewById(R.id.cw_dialog_cancel_button)
        mSure = v.findViewById(R.id.cw_dialog_sure_button)
        mIcon = v.findViewById(R.id.cw_dialog_icon)
        mAlone = v.findViewById(R.id.cw_dialog_alone_button)

        configureSubviews()

        return v
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val dm = DisplayMetrics()
            activity!!.windowManager!!.defaultDisplay.getMetrics(dm)
            it.window.setLayout((dm.widthPixels * 0.75).toInt(),ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }


    private fun configureSubviews(){
        mSure?.setOnClickListener {
            mCallback?.onClickButton(true,this)
        }

        mCancel?.setOnClickListener {
            mCallback?.onClickButton(false,this)
        }

        mAlone?.setOnClickListener {
            mCallback?.onClickButton(true,this)
        }

        mBuilder?.let {
            mTitle?.text = it.getTitle()
            mDesc?.text = it.getDesc()
            val resId = it.getImage()
            resId?.let {
                mIcon?.setImageResource(it)
            }

            when (it.getType()){
                CWDialogType.Other -> {
                    hiddenAloneButton(true)
                    hiddenSureAndCancelButton(false)
                }
                CWDialogType.Error -> {
                    hiddenAloneButton(false)
                    hiddenSureAndCancelButton(true)
                }
                CWDialogType.Hint -> {
                    hiddenSureAndCancelButton(true)
                    hiddenAloneButton(false)
                }
            }
        }
    }


    private fun hiddenAloneButton(arg:Boolean){
        val flag = if (arg) View.GONE else View.VISIBLE
        mAlone?.visibility = flag
    }

    private fun hiddenSureAndCancelButton(arg:Boolean){
        val flag = if (arg) View.GONE else View.VISIBLE
        mSure?.visibility = flag
        mCancel?.visibility = flag
    }



    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        //todo:~~做一些操作 比如晴空接口
        mCallback = null
    }

    class Builder{

        private var mTitle:String? = null
        private var mDesc:String? = null
        private var mResId:Int? = null
        private var mType:CWDialogType = CWDialogType.Other//默认

        fun setTitle(arg:String):Builder{
            mTitle = arg
            return this
        }

        fun setDesc(arg: String):Builder{
            mDesc = arg
            return this
        }

        fun setImage(resId:Int):Builder{
            mResId = resId
            return this
        }

        fun setType(arg: CWDialogType) :Builder{
            mType = arg
            return this
        }

        fun getTitle():String?{
            return mTitle
        }

        fun getDesc():String?{
            return mDesc
        }

        fun getImage():Int?{
            return mResId
        }

        fun getType():CWDialogType{
            return mType
        }

        fun create():CWDialog{
            val dialog = CWDialog()

            dialog.mBuilder = this

            return dialog
        }
    }
}