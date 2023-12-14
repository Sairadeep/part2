package com.anxer.part2

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.anxer.part1.IOneAidlInterface

class MyPart2Service : Service() {
    private var aidlInterface: IOneAidlInterface? = null
    private lateinit var periodicRunnable: Runnable
    private var handler = Handler(Looper.getMainLooper())
    private var isPart3ServiceRunning = false
    private var isPart4ServiceRunning = false

    override fun onCreate() {
        periodicRunnable = object : Runnable {
            override fun run() {
                checkConnection()
                handler.postDelayed(this, 1000)
            }
        }
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val part3ServiceStart = Intent("MyPart3Service")
        part3ServiceStart.setPackage("com.anxer.part3")
        val part4SerStart = Intent("MyPart4Service")
        part4SerStart.setPackage("com.anxer.part4")
        isPart3ServiceRunning = IsServiceOn.isServiceRunning(this, part3ServiceStart::class.java)
        isPart4ServiceRunning = IsServiceOn.isServiceRunning(this, part4SerStart::class.java)
        if (!isPart3ServiceRunning && !isPart4ServiceRunning) {
            startService(part3ServiceStart)
            startService(part4SerStart)
        } else Log.d(StringData.tagName, StringData.serviceRunMessage)
        handler.post(periodicRunnable)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(periodicRunnable)
    }

    private fun checkConnection() {
        if (aidlInterface == null) {
            val intent = Intent("Part1Service")
            intent.setPackage("com.anxer.part1")
            bindService(intent, part1Connect, BIND_AUTO_CREATE)
            return
        }
        val nameReceived = aidlInterface?.sendName()
        if (nameReceived != "Default") Part1Data.setPart1Name(nameReceived.toString())
        val eoReceived = aidlInterface?.sendNumber()
        if (eoReceived != null && eoReceived != 2) ReceivedNoFromPart1.setPart1EO(eoReceived.toInt())
        if (ResponseCallBack.getResponseCallBack() == 1) aidlInterface?.checkBack(1) else aidlInterface?.checkBack(
            0
        )
        if (ResponseCallForEO.getResponseCallBackEO() == 2) aidlInterface?.checkEvenOddCallBack(2) else aidlInterface?.checkEvenOddCallBack(
            3
        )
    }

    private val part1Connect = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            aidlInterface = IOneAidlInterface.Stub.asInterface(p1)
            checkConnection()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            unbindService(this)
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private var binder = object : ITwoAidlInterface.Stub() {
        override fun sendNameTo3(): String {
            return Part1Data.getPart1Name()
        }

        override fun callBack(response: Int) {
            ResponseCallBack.setResponseCallBack(response)
        }

        override fun sendEO(): Int {
            return ReceivedNoFromPart1.getPart1EO()
        }

        override fun callBackOfEO(resCall: Int) {
            ResponseCallForEO.setResponseCallBackEO(resCall)
        }
    }

}