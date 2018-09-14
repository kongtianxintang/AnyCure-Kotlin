package com.example.chitwing.anycure_kotlin_master.model

import com.google.gson.annotations.SerializedName

class SMSCode {
    var code:Int = 0
    var msg:String? = null
    var data:VerificationCode? = null
}
class VerificationCode {
    @SerializedName("verificationcode")
    var code:String? = null
}