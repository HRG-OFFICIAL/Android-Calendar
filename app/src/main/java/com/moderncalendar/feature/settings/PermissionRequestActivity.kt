package com.moderncalendar.feature.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.moderncalendar.ui.theme.ModernCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionRequestActivity : ComponentActivity() {
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            finish()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            ModernCalendarTheme {
                PermissionRequestScreen(
                    onRequestPermissions = { requestPermissions() },
                    onSkip = { finish() }
                )
            }
        }
    }
    
    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.POST_NOTIFICATIONS
        )
        
        requestPermissionLauncher.launch(permissions)
    }
}

@Composable
fun PermissionRequestScreen(
    onRequestPermissions: () -> Unit,
    onSkip: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Permissions Required",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Modern Calendar needs the following permissions to function properly:",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("📅 Calendar Access", style = MaterialTheme.typography.titleMedium)
                Text("Read and write calendar events")
                
                Text("🔔 Notifications", style = MaterialTheme.typography.titleMedium)
                Text("Send event reminders and notifications")
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onRequestPermissions,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Grant Permissions")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        TextButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Skip for now")
        }
    }
}
