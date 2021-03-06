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
import com.example.chitwing.anycure_kotlin_master.activity.bind.BindActivity
import com.example.chitwing.anycure_kotlin_master.activity.prepare.OtPrepareActivity
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.dialog.BleDialog
import com.example.chitwing.anycure_kotlin_master.dialog.BleDialogInterface
import com.example.chitwing.anycure_kotlin_master.dialog.CWDialog
import com.example.chitwing.anycure_kotlin_master.dialog.CWDialogInterface
import com.example.chitwing.anycure_kotlin_master.fragment.BaseFragment
import com.example.chitwing.anycure_kotlin_master.model.BindDevice
import com.example.chitwing.anycure_kotlin_master.model.Recipe
import com.example.chitwing.anycure_kotlin_master.model.RecipeSection
import com.example.chitwing.anycure_kotlin_master.ot.RecipeInterface
import com.example.chitwing.anycure_kotlin_master.ot.RecipeSectionAdapter
import com.example.chitwing.anycure_kotlin_master.unit.CWUserActionLogManager
import com.example.chitwing.anycure_kotlin_master.unit.showToast
import com.orhanobut.logger.Logger


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
            val intent = Intent(activity!!, OtPrepareActivity ::class.java)
            startActivityForResult(intent,0x01)
        }

        override fun onClickButton(flag: Boolean) {
            Logger.d("点击了dialog cancel button")
        }
    }

    private var mRecipe:Recipe? = null

    private val onClickItem = object :RecipeInterface {
        override fun didSelectItem(obj: Recipe) {
            mRecipe = obj
            Logger.d("处方->${obj.recipeId} name->${obj.recipeName}")
            requestLocationPermission()
        }
    }

    /**
     * 推出弹框
     * */

    private fun pushScannerDialog(){
        if (mRecipe == null){ return }
        //todo: 判断是否有绑定的设备 如果没有则去提示用户绑定设备
        val list = DBHelper.findAll(BindDevice ::class.java)
        if (list == null || list.isEmpty()){//提示用户去绑定设备
            val dialog = CWDialog.Builder().setTitle("提示").setDesc("未绑定设备,无法进行方案").create()
            dialog.setCallback(mCWDialogCallback)
            dialog.show(activity!!.fragmentManager,"nullDevice")
        }else{
            mRecipe?.let {
                val dialog = BleDialog()
                dialog.setRecipe(it)
                dialog.setCallback(onBleDiaCallback)
                dialog.show(activity!!.fragmentManager,"bleDialog")
            }
        }
    }

    private val mCWDialogCallback = object :CWDialogInterface {
        override fun onClickButton(flag: Boolean, item: CWDialog) {
            if (flag){
                val intent = Intent(activity!!,BindActivity ::class.java)
                activity!!.startActivity(intent)
            }
            item.dismiss()
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
                    val str = if (it == PackageManager.PERMISSION_GRANTED) "同意定位权限" else "拒绝定位权限"
                    CWUserActionLogManager.permissionRequest(mapOf(CWUserActionLogManager.locationPermission to str))
                }
            }
            else -> {

            }
        }
    }

}
