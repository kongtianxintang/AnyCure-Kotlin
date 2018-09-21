package com.example.chitwing.anycure_kotlin_master.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * 基础的fragment
 * */
abstract class BaseFragment : Fragment() {


    val mTag:String by lazy {
        return@lazy "${this ::class.java}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(mTag,"onCreate")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(mTag,"onActivityCreated")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d(mTag,"onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(mTag,"onDetach")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(mTag,"onDestroy")
    }
}
