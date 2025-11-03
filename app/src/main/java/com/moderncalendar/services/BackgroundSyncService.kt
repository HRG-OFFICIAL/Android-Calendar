package com.moderncalendar.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class BackgroundSyncService : Service() {
    @Inject
    lateinit var workManager: WorkManager

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startPeriodicSync()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        when (intent?.action) {
            ACTION_SYNC_NOW -> {
                syncNow()
            }
            ACTION_START_PERIODIC_SYNC -> {
                startPeriodicSync()
            }
            ACTION_STOP_SYNC -> {
                stopSync()
            }
        }
        return START_STICKY
    }

    private fun syncNow() {
        serviceScope.launch {
            try {
                // No-op for now to avoid unresolved dependency
            } catch (e: Exception) {
                // Log error
            }
        }
    }

    private fun startPeriodicSync() {
        val constraints =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(false)
                .build()

        val syncWorkRequest =
            PeriodicWorkRequestBuilder<SyncWorker>(
                1,
                TimeUnit.HOURS,
                15,
                TimeUnit.MINUTES,
            )
                .setConstraints(constraints)
                .build()

        workManager.enqueueUniquePeriodicWork(
            "background_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest,
        )
    }

    private fun stopSync() {
        workManager.cancelUniqueWork("background_sync")
    }

    companion object {
        const val ACTION_SYNC_NOW = "com.moderncalendar.action.SYNC_NOW"
        const val ACTION_START_PERIODIC_SYNC = "com.moderncalendar.action.START_PERIODIC_SYNC"
        const val ACTION_STOP_SYNC = "com.moderncalendar.action.STOP_SYNC"
    }
}

// Temporarily disable Hilt wiring to isolate KSP failures
class SyncWorker(
    appContext: android.content.Context,
    params: androidx.work.WorkerParameters,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): ListenableWorker.Result {
        return try {
            // TODO: Re-enable once Hilt graph is green
            ListenableWorker.Result.success()
        } catch (e: Exception) {
            ListenableWorker.Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "sync_work"
    }
}
