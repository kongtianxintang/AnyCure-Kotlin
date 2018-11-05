package com.example.chitwing.anycure_kotlin_master.unit

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import com.example.chitwing.anycure_kotlin_master.BuildConfig
import com.example.chitwing.anycure_kotlin_master.app.MyApp
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CWUserActionLogManager {


    const val locationPermission = "定位权限"
    const val bluetoothPermisson = "蓝牙权限"
    /**
     * 用户权限统计
     * */
    fun permissionRequest( map: Map<String,String>){
        val js = mutableMapOf<String,String>()
        js.putAll(map)
        js.putAll(createDeviceInfo())
        val call = NetRequest.userActionLog(js)
        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {

            }

            override fun onResponse(call: Call<String>, response: Response<String>) {

            }
        })
    }

    /**
     * 设备信息
     * board:品牌
     * release:系统版本号
     * model:设备型号
     * */
    private fun createDeviceInfo(): Map<String,String>{
        val map = mutableMapOf<String,String>()
        Build.BOARD.let {
            map.put("board",it)
        }

        Build.VERSION.RELEASE?.let {
            map.put("release",it)
        }

        Build.MODEL?.let {
            map.put("model",it)
        }

        return map
    }
}