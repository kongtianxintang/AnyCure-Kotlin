package com.example.chitwing.anycure_kotlin_master.activity.prepare

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.unit.loader
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
            Log.d("测试 help->","${it.recipe?.getFinalRecipeHelp()}")
            //todo:电极贴法
            //注意事项

            //根据partId区分 精品推荐or处方库 0:精品推荐  1:处方库
            //处方库 无电极贴法
            when (it.recipe!!.partId){
                0 -> {
                    topSegment.setText("方案综述","电极贴法")
                }
                1 -> {
                    topSegment.setText("方案综述")
                }
                else -> {}
            }

            //设置回调
            it.mCallback = mProvider.deviceInterface
        }
        switchTopSegment(0)
        switchMidSegment(0)
    }


    private fun switchTopSegment(arg:Int){
        when (arg) {
            0 -> {
                recyclerView.visibility = View.GONE
                reviewEditText.visibility = View.VISIBLE
            }
            else -> {
                recyclerView.visibility = View.VISIBLE
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
