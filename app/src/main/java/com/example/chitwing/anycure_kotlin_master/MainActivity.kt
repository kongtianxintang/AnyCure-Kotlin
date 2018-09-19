package com.example.chitwing.anycure_kotlin_master

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.activity.prepare.PrepareProvider
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.fragment.cure.CureFragment
import com.example.chitwing.anycure_kotlin_master.fragment.mall.MallFragment
import com.example.chitwing.anycure_kotlin_master.fragment.mine.MineFragment
import com.example.chitwing.anycure_kotlin_master.fragment.otCure.OtCureFragment
import com.example.chitwing.anycure_kotlin_master.fragment.recipe.RecipeFragment
import com.example.chitwing.anycure_kotlin_master.unit.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.launch



class MainActivity : BaseActivity() {



    private var mBottomNavView:BottomNavigationView? = null
    /**
     * 首页的四个fragment
     * */
    private var mRecipeFragment:RecipeFragment? = null
    private var mCureFragment:OtCureFragment? = null
    private var mMallFragment:MallFragment? = null
    private var mMineFragment:MineFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        fetchData()

    }

    override fun initView() {
        mBottomNavView = findViewById(R.id.bottom_navigation)
        BottomNavigationViewHelper.disableShiftMode(mBottomNavView!!)

        defaultBottomNavigationView()

        defaultData()
    }

    override fun fetchData() {

        PrepareProvider(this).fetchDataSource()

    }

    /**
     * 配置底部导航条的点击事件
     * */
    private fun defaultBottomNavigationView(){
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
                    this.supportActionBar?.title = "我的"
                    if (mMineFragment == null){
                        mMineFragment = MineFragment()
                        transaction.add(R.id.linear_view,mMineFragment!!)
                    }
                    showItem = mMineFragment!!
                }
                R.id.action_recipe -> {
                    this.supportActionBar?.title = "首页"
                    showItem = mRecipeFragment!!
                }
                R.id.action_cure -> {
                    this.supportActionBar?.title = "控制器"
                    if (mCureFragment == null){
                        mCureFragment = OtCureFragment()
                        transaction.add(R.id.linear_view,mCureFragment!!)
                    }
                    showItem = mCureFragment!!
                }
                else -> {
                    this.supportActionBar?.title = "发现"
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
        this.title = "首页"
        val ac = supportActionBar
        ac?.setDisplayHomeAsUpEnabled(false)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(mTag,"requestCode->$requestCode  resultCode->$resultCode")
        if (resultCode == 0x01){
            switchCure()
        }
    }

    /**
     * 切换到理疗页
     * */
    private fun switchCure(){

        mCureFragment?.let {
            val last = CWBleManager.mCWDevices.lastOrNull()
            last?.let {
                mCureFragment!!.switchItem(it)
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

}
