package com.kamerlin.leon.alarmmanager_jobscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println("BootReceiver")


        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            println(Intent.ACTION_BOOT_COMPLETED)

        }

        //setAlarm(context)
        setAlarmService(context)
    }

    fun setAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (1000 * 60).toLong(), pendingIntent)
    }

    fun setAlarmService(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmService::class.java)
        val pendingIntent = PendingIntent.getService(context, 0, intent, 0)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (1000 * 60).toLong(), pendingIntent)
    }

    companion object {
        private val TAG = BootReceiver::class.java.simpleName
    }
}
