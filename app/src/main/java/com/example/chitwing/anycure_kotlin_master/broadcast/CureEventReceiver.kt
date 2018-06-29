package com.example.chitwing.anycure_kotlin_master.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class CureEventReceiver : BroadcastReceiver() {

    private val tag = "CureEventReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras
        val value = extras["flag"]
        Log.e(tag,intent.toString() + value.toString())
    }
}
