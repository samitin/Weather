package ru.samitin.weather.experement

import android.app.IntentService
import android.content.Intent
import android.util.Log

private const val TAG="MainServiceTAG"
const val MAIN_SERVICE_STRING_EXTRA = "MainServiceExtra"

class MainService (name: String = "MainService"): IntentService(name){

    override fun onHandleIntent(intent: Intent?) {
        createLogMassage("onHandleIntant ${intent?.getStringExtra(MAIN_SERVICE_STRING_EXTRA)}")
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