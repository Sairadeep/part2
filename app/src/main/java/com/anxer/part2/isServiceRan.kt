package com.anxer.part2

import android.app.ActivityManager
import android.content.Context

//import android.app.ActivityManager
//import android.content.Context

object Part1Data {
    private var part1ReceivedValue = "Default Part1"

    fun setPart1Name(name: String) {
        part1ReceivedValue = name
    }

    fun getPart1Name() = part1ReceivedValue
}

object ResponseCallBack {
    private var responseCallBack: Int = 100

    fun setResponseCallBack(responseCallBackValue: Int) {
        responseCallBack = responseCallBackValue
    }

    fun getResponseCallBack() = responseCallBack
}

object ReceivedNoFromPart1 {
    private var part1NumValue = 2

    fun setPart1EO(eo: Int) {
        part1NumValue = eo
    }

    fun getPart1EO() = part1NumValue
}

object ResponseCallForEO {
    private var responseCallEO: Int = 99

    fun setResponseCallBackEO(callBackEO: Int) {
        responseCallEO = callBackEO
    }

    fun getResponseCallBackEO() = responseCallEO
}

object IsServiceOn {
    @Suppress("Deprecation")
    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = manager.getRunningServices(Int.MAX_VALUE)
        for (service in services) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}

object StringData{
    var tagName : String = "part2Service"
    var serviceRunMessage: String = "Services are already running."
}



