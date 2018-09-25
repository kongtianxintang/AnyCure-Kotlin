package com.example.chitwing.anycure_kotlin_master.activity.prepare

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.model.Recipe
import com.example.chitwing.anycure_kotlin_master.model.RecipeUsage
import com.example.chitwing.anycure_kotlin_master.unit.loader
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_ot_prepare.*

class OtPrepareActivity : BaseActivity() {

    /**
     * 数据处理类
     * */
    private val mProvider:OtPrepareProvider by lazy {
        return@lazy OtPrepareProvider(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ot_prepare)
        initView()
        fetchData()
    }

    override fun initView() {
        topSegment.setOnSegmentControlClickListener {
            switchTopSegment(it)
        }
        midSegment.setOnSegmentControlClickListener {
            switchMidSegment(it)
        }
        prepareButton.setOnClickListener {
            val last = CWBleManager.mCWDevices.lastOrNull()
            last?.gattWrite?.cwBleWriteElectrodeQuery()
        }
        customTitle?.text = getText(R.string.ot_prepare_title)
    }

    override fun fetchData() {

        mProvider.fetchDataSource()

        val last = CWBleManager.mCWDevices.lastOrNull()
        last?.let {
            //标题
            recipeTitle.text = it.recipe?.recipeName
            //处方icon
            recipeIcon.loader(this,it.recipe?.recipeIcon)
            //处方的综述
            reviewEditText.text = it.recipe?.getFinalRecipeHelp()
            //注意事项
            //根据partId区分 精品推荐or处方库 0:精品推荐  1:处方库
            //处方库 无电极贴法
            when (it.recipe!!.partId){
                0 -> {
                    topSegment.setText("方案综述","电极贴法")
                    //电极贴法
                    configureRecyclerView(it.recipe)
                }
                1 -> {
                    topSegment.setText("方案综述")
                }
                else -> {}
            }

            //设置回调
            it.mCallback = mProvider.deviceInterface
            it.writeData()

        }
        switchTopSegment(0)
        switchMidSegment(0)
    }

    private fun configureRecyclerView(item:Recipe?){
        item?.recipeUsage?.let {
            val list = Gson().fromJson(it,Array<RecipeUsage> ::class.java)
            val adapter = OtPrepareAdapter(this,list)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
            val pagerSnap = PagerSnapHelper()
            pagerSnap.attachToRecyclerView(recyclerView)
            recyclerView.addOnScrollListener(listener)
            flagLabel.text = "1/${list.count()}"
        }
    }

    private val listener = object :RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when(newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    val m = recyclerView!!.layoutManager as LinearLayoutManager
                    val index = m.findFirstVisibleItemPosition()
                    val count = m.itemCount
                    flagLabel.text = "${index + 1}/$count"
                }
                RecyclerView.SCROLL_STATE_DRAGGING -> {  }
                RecyclerView.SCROLL_STATE_SETTLING -> {  }
            }
        }
    }

    private fun switchTopSegment(arg:Int){
        when (arg) {
            0 -> {
                recyclerView.visibility = View.GONE
                flagLabel.visibility = View.GONE
                reviewEditText.visibility = View.VISIBLE
            }
            else -> {
                recyclerView.visibility = View.VISIBLE
                flagLabel.visibility = View.VISIBLE
                reviewEditText.visibility = View.GONE
            }
        }

    }

    private fun switchMidSegment(arg:Int){
        mProvider.switchIndex(arg)
    }


    /**
     * 销毁的时候 删除最新链接的一个外设
     * */
    override fun onBackPressed() {
        super.onBackPressed()
        /**
         * finish 先于 onBackPressed 执行
         * */
        val last = CWBleManager.mCWDevices.lastOrNull()
        last?.removeSelf()
    }


}
