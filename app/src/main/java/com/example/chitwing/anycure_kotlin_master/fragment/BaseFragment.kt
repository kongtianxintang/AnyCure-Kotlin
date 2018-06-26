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


    val fm_tag:String by lazy {
        return@lazy "${this ::class.java}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(fm_tag,"onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(fm_tag,"onActivityCreated")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.e(fm_tag,"onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e(fm_tag,"onDetach")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(fm_tag,"onDestroy")
    }
}
