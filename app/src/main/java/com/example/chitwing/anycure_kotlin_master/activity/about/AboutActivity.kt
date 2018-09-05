package com.example.chitwing.anycure_kotlin_master.activity.about

import android.os.Bundle
import android.util.Log
import android.webkit.*
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.network.NetRequest
import com.google.gson.Gson
import kotlinx.android.synthetic.main.content_about.*

class AboutActivity : BaseActivity() {

    private lateinit var mWebView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        initView()
    }

    override fun initView() {

        mWebView = findViewById(R.id.about_web_view)

        val injects = InjectsObj()
        mWebView.addJavascriptInterface(injects,"aboutObj")

        //本地html
//        val path = "file:///android_asset/web/jr-about/about.html"
        //网络html
        val path = NetRequest.CW_HOST_IP + "/about/about.html"
        mWebView.loadUrl(path)

        /**
         * 对手机屏幕适应
         * */
        val setting = about_web_view.settings
        setting.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        setting.useWideViewPort = true
        setting.loadWithOverviewMode = true
        setting.javaScriptEnabled = true

        /**
         *
         * */
        val client = CWWebChromeClient()
        mWebView.webChromeClient = client
        mWebView.webViewClient = CWWebClient()

    }

    override fun fetchData() {

    }

    override fun onDestroy() {
        about_web_view.removeJavascriptInterface("aboutObj")
        super.onDestroy()
    }
    /**
     * 调用h5的js方法
     * */
    private fun evaluateJavascriptFunc(){
        val map = mapOf("code" to "0","channel" to "00000006","version" to "1.0")
        val json = Gson().toJson(map)
        val js = "javascript:updateInfo($json)"
        mWebView.evaluateJavascript(js,({
            Log.d("回调","字符串->$it")
        }))

    }


    inner class CWWebChromeClient:WebChromeClient (){

        override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
            Log.d("网页","message->$message")
            return super.onJsAlert(view, url, message, result)
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            Log.d("console","meg->${consoleMessage?.message()}")
            return super.onConsoleMessage(consoleMessage)
        }

    }


    inner class CWWebClient:WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            this@AboutActivity.evaluateJavascriptFunc()
        }
    }

    /**
     * 注入到h5中的方法
     * */
    inner class InjectsObj {
        @JavascriptInterface
        fun linkToDownApk(path: String){
            Log.d("注入信息","地址->$path")
        }
    }



}
