package com.example.chitwing.anycure_kotlin_master

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.activity.prepare.PrepareProvider
import com.example.chitwing.anycure_kotlin_master.broadcast.CureEventReceiver
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.fragment.cure.CureFragment
import com.example.chitwing.anycure_kotlin_master.fragment.mall.MallFragment
import com.example.chitwing.anycure_kotlin_master.fragment.mine.MineFragment
import com.example.chitwing.anycure_kotlin_master.fragment.recipe.RecipeFragment
import com.example.chitwing.anycure_kotlin_master.unit.BottomNavigationViewHelper
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


class MainActivity : BaseActivity() {

    companion object {
        val cureEvent = "cureEvent"
    }


    private var mBottomNavView:BottomNavigationView? = null
    /**
     * 首页的四个fragment
     * */
    private var mRecipeFragment:RecipeFragment? = null
    private var mCureFragment:CureFragment? = null
    private var mMallFragment:MallFragment? = null
    private var mMineFragment:MineFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val job = launch {
            initView()
            fetchData()
        }
        job.start()
    }

    override fun initView() {
        mBottomNavView = findViewById(R.id.bottom_navigation)
        BottomNavigationViewHelper.disableShiftMode(mBottomNavView!!)

        switchTab()

        defaultData()
    }

    override fun fetchData() {

        PrepareProvider(this).fetchDataSource()

    }

    private fun switchTab(){
        mBottomNavView!!.setOnNavigationItemSelectedListener {
            /**
             * 选择同样的fragment 不切换
             * */
            val currentItem = mBottomNavView!!.selectedItemId
            val futureItem = it.itemId
            if (currentItem == futureItem){
                return@setOnNavigationItemSelectedListener false
            }

            val transaction = supportFragmentManager.beginTransaction()
            val showItem:BaseFragment

            when(futureItem){
                R.id.action_mine -> {
                    if (mMineFragment == null){
                        mMineFragment = MineFragment()
                        transaction.add(R.id.linear_view,mMineFragment!!)
                    }
                    showItem = mMineFragment!!
                }
                R.id.action_recipe -> {
                    showItem = mRecipeFragment!!
                }
                R.id.action_cure -> {
                    if (mCureFragment == null){
                        mCureFragment = CureFragment()
                        transaction.add(R.id.linear_view,mCureFragment!!)
                    }
                    showItem = mCureFragment!!
                }
                else -> {
                    if (mMallFragment == null){
                        mMallFragment = MallFragment()
                        transaction.add(R.id.linear_view,mMallFragment!!)
                    }
                    showItem = mMallFragment!!
                }
            }

            val hideItem = when(currentItem) {
                R.id.action_recipe ->  mRecipeFragment!!
                R.id.action_cure ->  mCureFragment!!
                R.id.action_mall ->  mMallFragment!!
                else ->  mMineFragment!!
            }
            transaction.hide(hideItem)
            transaction.show(showItem)
            transaction.commit()
            return@setOnNavigationItemSelectedListener true
        }
    }

    /**
     * 基础数据
     * */
    private fun defaultData(){
        val transaction = supportFragmentManager.beginTransaction()
        mRecipeFragment = RecipeFragment()
        transaction.add(R.id.linear_view,mRecipeFragment!!)
        transaction.show(mRecipeFragment!!)
        transaction.commit()
        this.title = "处方"
        val ac = supportActionBar
        ac?.setDisplayHomeAsUpEnabled(false)

    }

    private var mReceiver:CureEventReceiver? = null
    override fun onPause() {
        super.onPause()
        unregisterReceiver(mReceiver!!)
    }

    override fun onResume() {
        super.onResume()
        mReceiver = CureEventReceiver()
        val filter = IntentFilter(cureEvent)
        registerReceiver(mReceiver!!,filter)

        val job = launch {
            delay(1000)
            val intent = Intent()
            intent.action = cureEvent
            intent.putExtra("flag","false")
            sendBroadcast(intent)
        }
        job.start()
    }




}
