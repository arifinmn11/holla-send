package com.enigma.application.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.widget.Toast


class TrackingService : JobService() {
    override fun onStartJob(p0: JobParameters?): Boolean {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
        // perform work here, i.e. network calls asynchronously

        // returning false means the work has been done, return true if the job is being run asynchronously
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {

        return false
    }

}