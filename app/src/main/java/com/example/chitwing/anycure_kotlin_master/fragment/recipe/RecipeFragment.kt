package com.example.chitwing.anycure_kotlin_master.fragment.recipe

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.base.CWOnItemClickListener
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.model.Recipe


/**
 * 处方页
 */
class RecipeFragment : BaseFragment() {


    private var mRecyclerView:RecyclerView? = null
    private var mProvider:RecipeProvider? = null
    var refreshView:SwipeRefreshLayout? = null
    var mAdapter:RecipeAdapter? = null
    val mDataSet = mutableListOf<Recipe>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mProvider = RecipeProvider(this.context!!,this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.e(fm_tag,"onCreateView")
        val view = inflater.inflate(R.layout.fragment_recipe, container, false)
        mRecyclerView = view.findViewById(R.id.fm_recipe_recycler_view)
        refreshView = view.findViewById(R.id.fm_recipe_refresh)

        configureView()

        return view
    }

    /**
     * 配置页面
     * */
    private fun configureView(){
        mRecyclerView!!.layoutManager = LinearLayoutManager(this.context!!,LinearLayoutManager.VERTICAL,false)

        val list = DBHelper.findAll(Recipe ::class.java)
        list?.let {
            mDataSet.addAll(it)
        }
        mAdapter = RecipeAdapter(mDataSet,this.context!!)
        mRecyclerView!!.adapter = mAdapter!!
        mAdapter!!.onItemClickListenner = onClickItemCallback

        mProvider!!.fetchDataSource()

        refreshView!!.setOnRefreshListener {
            mProvider!!.fetchDataSource()
        }
    }


    private val onClickItemCallback = object : CWOnItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            Log.e(fm_tag,"位置$position")
        }
    }



}
