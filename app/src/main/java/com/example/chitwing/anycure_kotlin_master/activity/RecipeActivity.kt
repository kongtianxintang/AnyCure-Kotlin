package com.example.chitwing.anycure_kotlin_master.activity


import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.ble.CWBleStatus
import com.example.chitwing.anycure_kotlin_master.ble.CWBleStatusInterface

class RecipeActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
    }


    override fun initView() {
        initBle()
    }

    private fun initBle(){

        CWBleManager.setStatusCallback(statusCallback)
        CWBleManager.startScan()

    }

    private val statusCallback = object :CWBleStatusInterface {
        override fun bleStatus(arg: CWBleStatus) {
            when(arg){
                CWBleStatus.Disable -> openBle()
                else -> Log.d(tag,"其他情况:$arg")
            }
        }
    }

    private fun openBle(){
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            Log.d(tag,"请求开启蓝牙回调$requestCode,result:$resultCode")
        }
    }

}
