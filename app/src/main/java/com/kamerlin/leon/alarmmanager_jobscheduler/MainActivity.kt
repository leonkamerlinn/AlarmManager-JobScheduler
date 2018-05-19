package com.kamerlin.leon.alarmmanager_jobscheduler


import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import com.firebase.jobdispatcher.Constraint
import com.firebase.jobdispatcher.GooglePlayDriver
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.Trigger


class MainActivity : AppCompatActivity() {

    val alarmManager by lazy { getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    val i by lazy { Intent(this, BootReceiver::class.java) }
    val pendingIntent by lazy { PendingIntent.getBroadcast(this, 10, i, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startJobLolipop()
        } else {
            startJob()
        }

        setAlarm()
    }





    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startJobLolipop() {
        var jobIsAlreadyScheduled = false
        val componentName = ComponentName(this, MyJobService::class.java)
        val jobInfo = JobInfo.Builder(12, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        for (jobs in jobScheduler.allPendingJobs) {
            if (jobs.id == 12) {
                jobIsAlreadyScheduled = true
                break

            } else {
                jobIsAlreadyScheduled = false
            }
        }

        if (jobIsAlreadyScheduled) {
            Log.d(TAG, "Job is already scheduled")
        } else {
            val resultCode = jobScheduler.schedule(jobInfo)

            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d(TAG, "Job scheduled!")
            } else {
                Log.d(TAG, "Job not scheduled")
            }
        }
    }


    private fun startJob() {

        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))


        val myJob = dispatcher.newJobBuilder()
                .setService(FirebaseJobDispatcherService::class.java)
                // start between 0 and 10 seconds from now
                .setTrigger(Trigger.executionWindow(0, 10))
                .setConstraints(
                // only run on an unmetered network
                Constraint.ON_UNMETERED_NETWORK,
                // only run when the device is charging
                Constraint.DEVICE_CHARGING
        )
                .setTag("dispatcher")
                .build()


        dispatcher.mustSchedule(myJob)
        //dispatcher.cancel("dispatcher")
        //dispatcher.cancelAll()


    }

    private fun setBootReceiverEnabled(componentEnabledState: Int) {
        val componentName = ComponentName(this, BootReceiver::class.java)
        val packageManager = packageManager
        packageManager.setComponentEnabledSetting(componentName, componentEnabledState, PackageManager.DONT_KILL_APP)
    }

    private fun setAlarm() {
        Log.d(TAG, "AlarmReceiver set")
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)

        // Enable BootReceiver Component
        //setBootReceiverEnabled(PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
    }

    private fun cancelAlarm() {
        Log.d(TAG, "AlarmReceiver cancelled")
        alarmManager.cancel(pendingIntent)

        // Disable BootReceiver Component
        //setBootReceiverEnabled(PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
