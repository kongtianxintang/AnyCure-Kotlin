package com.example.chitwing.anycure_kotlin_master.fragment.recipe

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.prepare.OtPrepareActivity
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.dialog.BleDialog
import com.example.chitwing.anycure_kotlin_master.dialog.BleDialogInterface
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.model.Recipe
import com.example.chitwing.anycure_kotlin_master.model.RecipeSection
import com.example.chitwing.anycure_kotlin_master.ot.RecipeInterface
import com.example.chitwing.anycure_kotlin_master.ot.RecipeSectionAdapter
import com.example.chitwing.anycure_kotlin_master.unit.showToast


/**
 * 处方页
 */
class RecipeFragment : BaseFragment() {


    private var mRecyclerView:RecyclerView? = null
    private var mProvider:RecipeProvider? = null
    var refreshView:SwipeRefreshLayout? = null
    var mAdapter:RecipeSectionAdapter? = null
    val mDataSet = mutableListOf<RecipeSection>()


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
        val data = listOf( RecipeSection(0x00,list!!.filter { it.partId == 0 }),
                RecipeSection(true,"精品",false),
                RecipeSection(0x02,list.filter { it.partId == 0 }),
                RecipeSection(true,"处方库",false),
                RecipeSection(0x03,list.filter { it.partId == 1 }))
        mDataSet.addAll(0,data)
        mAdapter = RecipeSectionAdapter(activity!!,mDataSet)
        mAdapter!!.callback = onClickItem
        mRecyclerView!!.adapter = mAdapter

        mProvider!!.fetchDataSource()

        refreshView!!.setOnRefreshListener {
            mProvider!!.fetchDataSource()
        }
    }

    private val onBleDiaCallback = object :BleDialogInterface {
        override fun connectDevice() {
            Log.e("测试","走了几次")
            val intent = Intent(activity!!, OtPrepareActivity ::class.java)
            startActivityForResult(intent,0x01)
        }
    }

    private var mRecipe:Recipe? = null

    private val onClickItem = object :RecipeInterface {
        override fun didSelectItem(obj: Recipe) {
            mRecipe = obj
            Log.d("选择的","处方名->${obj.recipeName}")
            requestLocationPermission()
        }
    }

    /**
     * 推出弹框
     * */

    private fun pushScannerDialog(){
        mRecipe?.let {
            val dialog = BleDialog()
            dialog.setRecipe(it)
            dialog.showBleDialog(activity!!.supportFragmentManager)
            dialog.setCallback(onBleDiaCallback)
        }
    }

    /**
     * 检测是否支持蓝牙 及 是否打开蓝牙
     * */
    private fun checkBleSupported(){
        val m = activity?.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        val adapter = m?.adapter
        if (adapter == null){
            activity?.showToast("设备不支持蓝牙")
        }else{
            if (adapter.isEnabled){
                pushScannerDialog()
            }else{
                val on = adapter.enable()
                when (on){
                    true -> { pushScannerDialog() }
                    else -> { activity?.showToast("打开蓝牙失败,重试") }
                }
            }
        }
    }

    /**
     * 请求定位权限
     * */
    private fun requestLocationPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (activity?.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),1)
            }else{
                checkBleSupported()
            }
        }else{
            checkBleSupported()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                grantResults.firstOrNull()?.let {
                    if (it == PackageManager.PERMISSION_GRANTED){
                        checkBleSupported()
                    }else{
                        activity?.showToast("请求权限失败")
                    }
                }
            }
            else -> {

            }
        }
    }

}
