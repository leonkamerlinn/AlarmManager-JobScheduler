package com.kamerlin.leon.alarmmanager_jobscheduler

import android.app.IntentService
import android.content.Intent

class AlarmService: IntentService(AlarmService::class.java.simpleName) {
    override fun onHandleIntent(intent: Intent?) {
        println("Alarm from IntentService!!!")
    }
}