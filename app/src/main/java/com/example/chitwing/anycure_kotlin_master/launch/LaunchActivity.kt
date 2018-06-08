package com.example.chitwing.anycure_kotlin_master.launch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.chitwing.anycure_kotlin_master.MainActivity
import com.example.chitwing.anycure_kotlin_master.R
import com.example.chitwing.anycure_kotlin_master.activity.BaseActivity
import com.example.chitwing.anycure_kotlin_master.activity.LoginActivity
import com.example.chitwing.anycure_kotlin_master.activity.RecipeActivity
import com.example.chitwing.anycure_kotlin_master.database.DBHelper
import com.example.chitwing.anycure_kotlin_master.model.Login

class LaunchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }
    /*进入 登录页/主页*/
    private fun enterDisplay(){
        val log = DBHelper.find(1,this,Login ::class.java)
        val intent = createIntent(log == null)
        startActivity(intent)
        finish()
    }

    override fun initView() {

    }

    private fun createIntent(arg :Boolean):Intent{
        when (arg){
            true -> return Intent(this,LoginActivity ::class.java)
            else -> return Intent(this,RecipeActivity ::class.java)
        }
    }


    override fun onResume() {
        super.onResume()
        Thread.sleep(500)
        enterDisplay()
    }


}
