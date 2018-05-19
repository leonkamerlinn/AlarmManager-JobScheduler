package com.kamerlin.leon.alarmmanager_jobscheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class MyJobService : JobService() {
    private var isWorking = false
    private var jobCancelled = false

    // Called by the Android system when it's time to run the job
    override fun onStartJob(jobParameters: JobParameters): Boolean {
        Log.d(TAG, "Job started!")
        isWorking = true
        // We need 'jobParameters' so we can call 'jobFinished'
        startWorkOnNewThread(jobParameters) // Services do NOT run on a separate thread

        return isWorking
    }

    private fun startWorkOnNewThread(jobParameters: JobParameters) {
        Thread(Runnable { doWork(jobParameters) }).start()
    }

    private fun doWork(jobParameters: JobParameters) {
        // 10 seconds of 'working' (1000*10ms)
        for (i in 0..19) {
            Log.d(TAG, String.format("done %s", i))
            // If the job has been cancelled, stop working; the job will be rescheduled.
            if (jobCancelled)
                return

            try {
                Thread.sleep(1000)
            } catch (e: Exception) {
            }

        }

        Log.d(TAG, "Job finished!")
        isWorking = false
        val needsReschedule = false

        jobFinished(jobParameters, needsReschedule)
    }

    // Called if the job was cancelled before being finished
    override fun onStopJob(jobParameters: JobParameters): Boolean {
        Log.d(TAG, "Job cancelled before being completed.")
        jobCancelled = true
        val needsReschedule = isWorking
        jobFinished(jobParameters, needsReschedule)
        return needsReschedule
    }

    companion object {
        private val TAG = MyJobService::class.java.simpleName
    }
}