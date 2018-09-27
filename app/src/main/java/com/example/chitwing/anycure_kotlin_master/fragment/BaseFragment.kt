package com.example.chitwing.anycure_kotlin_master.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger


/**
 * 基础的fragment
 * */
abstract class BaseFragment : Fragment() {
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("onCreate")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Logger.d("onActivityCreated")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Logger.d("onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Logger.d("onDetach")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("onDestroy")
    }
}
