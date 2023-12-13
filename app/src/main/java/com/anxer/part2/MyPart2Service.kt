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
        Log.d("part2Service", "Part2 service is started from Part1, starting Part3,Part4 now")
        val part3ServiceStart = Intent("MyPart3Service")
        part3ServiceStart.setPackage("com.anxer.part3")
        val part4SerStart = Intent("MyPart4Service")
        part4SerStart.setPackage("com.anxer.part4")
        isPart3ServiceRunning = IsServiceOn.isServiceRunning(this, part3ServiceStart::class.java)
        isPart4ServiceRunning = IsServiceOn.isServiceRunning(this, part4SerStart::class.java)
        if (!isPart3ServiceRunning && !isPart4ServiceRunning) {
            startService(part3ServiceStart)
            startService(part4SerStart)
        } else Log.d("part2Service", "Requested services are already running.")
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
        Log.d("part2Service", "Received name from part1 : $nameReceived")
        if (nameReceived != "Default") Part1Data.setPart1Name(nameReceived.toString())
        val eoReceived = aidlInterface?.sendNumber()
        Log.d("part2Service", "Hi  ${eoReceived.toString()}")
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
            Log.d("part2ServiceConnectionStatus", "Service is connected.")
            aidlInterface = IOneAidlInterface.Stub.asInterface(p1)
            checkConnection()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d("part2ServiceConnectionStatus", "Service is disconnected.")
            unbindService(this)
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private var binder = object : ITwoAidlInterface.Stub() {
        override fun sendNameTo3(): String {
            Log.d("part2ServiceSent", "Sending from part2 to part3: ${Part1Data.getPart1Name()}")
            return Part1Data.getPart1Name()
        }

        override fun callBack(response: Int) {
            Log.d("part2Service", "ResponseCallBack: $response")
            ResponseCallBack.setResponseCallBack(response)
        }

        override fun sendEO(): Int {
            Log.d(
                "part2ServiceSent",
                "Sending from part2 to part: ${ReceivedNoFromPart1.getPart1EO()}"
            )
            return ReceivedNoFromPart1.getPart1EO()
        }

        override fun callBackOfEO(resCall: Int) {
            Log.d("part2Service", "ResponseCallEO: $resCall")
            ResponseCallForEO.setResponseCallBackEO(resCall)
        }
    }

}