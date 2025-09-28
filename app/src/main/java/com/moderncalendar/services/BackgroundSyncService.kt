package com.moderncalendar.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.*
import com.moderncalendar.core.sync.CloudSyncManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class BackgroundSyncService : Service() {
    
    @Inject
    lateinit var cloudSyncManager: CloudSyncManager
    
    @Inject
    lateinit var workManager: WorkManager
    
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        startPeriodicSync()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
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
                cloudSyncManager.syncEvents()
            } catch (e: Exception) {
                // Log error
            }
        }
    }
    
    private fun startPeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(false)
            .build()
        
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            1, TimeUnit.HOURS,
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            "background_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest
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

class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val cloudSyncManager: CloudSyncManager
) : CoroutineWorker(context, workerParams) {
    
    override suspend fun doWork(): Result {
        return try {
            cloudSyncManager.syncEvents()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
    
    companion object {
        const val WORK_NAME = "sync_work"
    }
}
