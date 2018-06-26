package com.example.chitwing.anycure_kotlin_master

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.fragment.cure.CureFragment
import com.example.chitwing.anycure_kotlin_master.fragment.mall.MallFragment
import com.example.chitwing.anycure_kotlin_master.fragment.mine.MineFragment
import com.example.chitwing.anycure_kotlin_master.fragment.recipe.RecipeFragment
import com.example.chitwing.anycure_kotlin_master.unit.BottomNavigationViewHelper


class MainActivity : BaseActivity() {

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
        initView()
    }

    override fun initView() {
        mBottomNavView = findViewById(R.id.bottom_navigation)
        BottomNavigationViewHelper.disableShiftMode(mBottomNavView!!)

        mBottomNavView!!.setOnNavigationItemSelectedListener {
            /**
             * 选择同样的fragment 不切换
             * */
            val currentItem = mBottomNavView!!.selectedItemId
            val futureItem = it.itemId
            if (currentItem == futureItem){
                return@setOnNavigationItemSelectedListener false
            }

            Log.e(tag,"选择的${mBottomNavView!!.selectedItemId} 当前的:${it.itemId}")

            val current:Int
            val future:Int

            when(futureItem){
                R.id.action_mine -> {
                    future = 3
                }
                R.id.action_recipe -> {
                    future = 0
                }
                R.id.action_cure -> {
                    future = 1
                }
                else -> {
                    future = 2
                }
            }

            when(currentItem){
                R.id.action_mine -> {
                    current = 3
                }
                R.id.action_recipe -> {
                    current = 0
                }
                R.id.action_cure -> {
                    current = 1
                }
                else -> {
                    current = 2
                }
            }
            switchTab(future,current)
            return@setOnNavigationItemSelectedListener true
        }

        defaultData()

    }

    override fun fetchData() {

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
    }

    /**
     * 切换tab
     * */
    private fun switchTab(index:Int,current:Int){
        val transaction = supportFragmentManager.beginTransaction()

        val item:BaseFragment
        val currentItem:BaseFragment
        when(index){
            0 -> {
                if (mRecipeFragment == null){
                    mRecipeFragment = RecipeFragment()
                    transaction.add(R.id.linear_view,mRecipeFragment!!)
                }
                item = mRecipeFragment!!
            }
            1 -> {
                if (mCureFragment == null){
                    mCureFragment = CureFragment()
                    transaction.add(R.id.linear_view,mCureFragment!!)
                }
                item = mCureFragment!!
            }
            2 -> {
                if (mMallFragment == null){
                    mMallFragment = MallFragment()
                    transaction.add(R.id.linear_view,mMallFragment!!)
                }
                item = mMallFragment!!
            }
            else -> {
                if (mMineFragment == null){
                    mMineFragment = MineFragment()
                    transaction.add(R.id.linear_view,mMineFragment!!)
                }
                item = mMineFragment!!
            }
        }

        when(current){
            0 -> currentItem = mRecipeFragment!!
            1 -> currentItem = mCureFragment!!
            2 -> currentItem = mMallFragment!!
            else -> currentItem = mMineFragment!!
        }

        transaction.hide(currentItem)
        transaction.show(item)
        transaction.commit()
    }



}
