package com.anxer.part2

//import android.app.ActivityManager
//import android.content.Context

object Part1Data{
    var part1ReceivedValue = "Default Part1"

    fun setPart1Name (name : String){
        part1ReceivedValue = name
    }

    fun getPart1Name() = part1ReceivedValue
}

//@Suppress("Deprecation")
//object isServiceRan {
//    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
//        val manager: ActivityManager =
//            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val services = manager.getRunningServices(Int.MAX_VALUE)
//        for (service in services) {
//            if (serviceClass.name == service.service.className) {
//                return true
//            }
//        }
//        return false
//    }
//}