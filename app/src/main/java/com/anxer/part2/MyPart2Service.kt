package com.anxer.part2

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.anxer.part1.IOneAidlInterface

class MyPart2Service : Service() {
    private var aidlInterface: IOneAidlInterface? = null
    private var isServiceRunning : Boolean =  false

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("part2Service", "Part2 service is started from Part1")
        isServiceRunning = true
        checkConnection()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        isServiceRunning = false
        super.onDestroy()
    }

    private fun checkConnection() {
        if (aidlInterface == null) {
            val intent = Intent("Part1Service")
            intent.setPackage("com.anxer.part1")
            //startService(intent)
            bindService(intent, part1Connect, BIND_AUTO_CREATE)
            return
        }
        val nameReceived = aidlInterface?.sendName()
        Log.d("part2Service", "Received name from part1 : $nameReceived")
        Part1Data.setPart1Name(nameReceived.toString())
        if (nameReceived != "Default") {
            aidlInterface?.checkBack(1)
        } else {
            aidlInterface?.checkBack(0)
        }
    }

    private val part1Connect = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d("part2ServiceConnectionStatus", "Service is connected.")
            aidlInterface = IOneAidlInterface.Stub.asInterface(p1)
            checkConnection()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d("part2ServiceConnectionStatus", "Service is disconnected.")
            unbindService(this)
        }
    }

}