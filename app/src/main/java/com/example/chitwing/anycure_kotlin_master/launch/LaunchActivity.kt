package com.example.chitwing.anycure_kotlin_master.launch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.chitwing.anycure_kotlin_master.BuildConfig
import com.example.chitwing.anycure_kotlin_master.MainActivity
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.LoginActivity
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.model.Login
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }
    /*进入 登录页/主页*/
    private fun enterDisplay(){
        val logs = DBHelper.findAll(Login ::class.java)
        val intent = createIntent(logs == null || logs.isEmpty())
        startActivity(intent)
        finish()
    }


    private fun createIntent(arg :Boolean):Intent{
       return  when (arg) {
            true ->  Intent(this,LoginActivity ::class.java)
            else ->  Intent(this, MainActivity ::class.java)
        }
    }


    override fun onResume() {
        super.onResume()
        Thread.sleep(500)
        enterDisplay()
    }



}
