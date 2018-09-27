package com.example.chitwing.anycure_kotlin_master

import android.content.Intent
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.activity.prepare.PrepareProvider
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.dialog.CWHintDialog
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.fragment.mall.MallFragment
import com.example.chitwing.anycure_kotlin_master.fragment.mine.MineFragment
import com.example.chitwing.anycure_kotlin_master.fragment.otCure.OtCureFragment
import com.example.chitwing.anycure_kotlin_master.fragment.recipe.RecipeFragment
import com.example.chitwing.anycure_kotlin_master.unit.BottomNavigationViewHelper
import com.example.chitwing.anycure_kotlin_master.unit.CWRegex
import com.example.chitwing.anycure_kotlin_master.unit.SharedPreferencesHelper
import com.orhanobut.logger.Logger
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


class MainActivity : BaseActivity() {



    private var mBottomNavView:BottomNavigationView? = null
    /**
     * 首页的四个fragment
     * */
    private val mRecipeFragment by lazy { return@lazy RecipeFragment() }
    private val mCureFragment by lazy { return@lazy OtCureFragment() }
    private val mMallFragment by lazy { return@lazy MallFragment() }
    private val mMineFragment by lazy { return@lazy MineFragment() }
    private var badgeView:View? = null
    private var mBadge:TextView? = null

    private val mFragmentTags by lazy {
        return@lazy listOf("mRecipe","mCure","mMall","mMine")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        fetchData()
        pushHintDialog()
    }

    /**
     * 问题：底部导航栏无法切换
     * 出现的情况
     * 1.当系统内存不足，Fragment 的宿主 Activity 回收的时候
     * 2.进入的Activity崩溃，返回页面时
     * 3....
     * 解决办法：在Fragment中所在的activity中重写onSaveInstanceState方法，并且把super.onSaveInstanceState(outState); 注释掉，阻止activity保存fragment的状态就可以了，
     */
    override fun onSaveInstanceState(outState: Bundle?) {}

    override fun initView() {
        mBottomNavView = findViewById(R.id.bottom_navigation)
        BottomNavigationViewHelper.disableShiftMode(mBottomNavView!!)

        defaultBottomNavigationView()

        defaultData()

        customLeftBar?.visibility = View.GONE
    }

    override fun fetchData() {
        PrepareProvider(this).fetchDataSource()
    }

    /**
     * 配置底部导航条的点击事件
     * */
    private fun defaultBottomNavigationView(){

        val menuView = mBottomNavView!!.getChildAt(0) as BottomNavigationMenuView
        val tab = menuView.getChildAt(1) as BottomNavigationItemView
        badgeView = LayoutInflater.from(this).inflate(R.layout.tips_badge,menuView,false)
        tab.addView(badgeView)
        mBadge = badgeView!!.findViewById(R.id.mBadgeTextView)
        cureBadge()

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
            val tagIndex: Int
            when(futureItem){
                R.id.action_mine -> {
                    customTitle?.text = "我的"
                    showItem = mMineFragment
                    tagIndex = 3
                }
                R.id.action_recipe -> {
                    customTitle?.text = "首页"
                    showItem = mRecipeFragment
                    tagIndex = 0
                }
                R.id.action_cure -> {
                    customTitle?.text = "控制器"
                    showItem = mCureFragment
                    tagIndex = 1
                }
                else -> {
                    customTitle?.text = "发现"
                    showItem = mMallFragment
                    tagIndex = 2
                }
            }

            val hideItem = when(currentItem) {
                R.id.action_recipe ->  mRecipeFragment
                R.id.action_cure ->  mCureFragment
                R.id.action_mall ->  mMallFragment
                else ->  mMineFragment
            }
            if (!showItem.isAdded){
                transaction.add(R.id.linear_view,showItem,mFragmentTags[tagIndex])
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
        if (!mRecipeFragment.isAdded){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.linear_view,mRecipeFragment,mFragmentTags[0])
            transaction.show(mRecipeFragment)
            transaction.commit()
            customTitle?.text = "首页"
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0x01){
            switchCure()
        }
    }

    /**
     * 切换到理疗页
     * */
    private fun switchCure(){

        if (mCureFragment.isAdded){
            val last = CWBleManager.mCWDevices.lastOrNull()
            last?.let {
                mCureFragment.switchItem(it)
            }
        }
        mBottomNavView!!.selectedItemId = R.id.action_cure
    }

    /**
     * 切换到处方页
     * */
    fun switchRecipeFragment(){
        mBottomNavView!!.selectedItemId = R.id.action_recipe
    }


    /**
     * 提示禁忌事项
     * */
    private fun pushHintDialog(){
        /**
         * 判断是否需要推出----
         * @return 如果false 则推出
         * */
        val  isIgnore = SharedPreferencesHelper.getObject(SharedPreferencesHelper.hintKey,false) as Boolean
        if (!isIgnore){
            val job = launch {
                delay(2000)
                val dialog = CWHintDialog()
                dialog.show(fragmentManager,"hint")
            }
            job.start()
        }
    }

    /**
     * 设置理疗页的角标
     * */
    fun cureBadge(){
        val num = CWBleManager.mCWDevices.count()
        val badgeVisibility = if (num == 0) View.INVISIBLE else View.VISIBLE
        badgeView?.visibility = badgeVisibility
        mBadge?.text = num.toString()
    }
}
