package com.example.chitwing.anycure_kotlin_master.activity.shared

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.ble.CWBleManager
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import com.example.chitwing.anycure_kotlin_master.unit.QRcodeUnit
import com.orhanobut.logger.Logger
import kotlinx.coroutines.experimental.launch

class SharedActivity : BaseActivity() {

    private var mQRCoder: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared)
        initView()
        fetchData()
        customTitle?.text = getString(R.string.shared_activity)
    }

    override fun fetchData() {

    }

    override fun initView() {

        mQRCoder = findViewById(R.id.mQRCode)

        val job = launch {
            val str = NetRequest.CW_HOST_IP + "/client/" + CWBleManager.configure.channel.NUM_CODE
            val s = R.mipmap.app_icon
            val b = BitmapFactory.decodeResource(resources,s)
            val bitmap = QRcodeUnit.createQRCodeWithLogo(str,b)
            runOnUiThread {
                mQRCoder?.setImageBitmap(bitmap)
            }
        }

        job.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("销毁->分享软件ac")
    }
}
