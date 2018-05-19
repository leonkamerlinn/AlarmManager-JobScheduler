package com.kamerlin.leon.alarmmanager_jobscheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println("Alarm!!!")
    }

    companion object {
        private val TAG = AlarmReceiver::class.java.simpleName
    }
}