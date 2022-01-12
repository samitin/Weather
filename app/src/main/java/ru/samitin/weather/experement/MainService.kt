package ru.samitin.weather.experement

import android.app.IntentService
import android.content.Intent
import android.util.Log

private const val TAG="MainServiceTAG"
const val MAIN_SERVICE_STRING_EXTRA = "MainServiceExtra"
const val MAIN_SERVICE_INT_EXTRA = "MAIN_SERVICE_INT_EXTRA"

class MainService (name: String = "MainService"): IntentService(name){

    override fun onHandleIntent(intent: Intent?) {
        intent?.let { sendBack(it.getIntExtra(MAIN_SERVICE_INT_EXTRA,0).toString()) }
        createLogMassage("onHandleIntant ${intent?.getStringExtra(MAIN_SERVICE_STRING_EXTRA)}")
    }

    private fun sendBack(resoult: String) {
        val broadcastIntent=Intent(TEST_BROADCAST_INTENT_FILTER)
        broadcastIntent.putExtra(THREADS_FRAGMENT_BROADCAST_EXTRA,resoult)
        sendBroadcast(broadcastIntent)
    }

    override fun onCreate() {
        createLogMassage("onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createLogMassage("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        createLogMassage("onDestroy")
        super.onDestroy()
    }

    private fun createLogMassage(message:String){
        Log.d(TAG,message)
    }
}