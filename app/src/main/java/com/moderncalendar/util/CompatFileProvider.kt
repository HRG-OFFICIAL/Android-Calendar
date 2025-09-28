package com.moderncalendar.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class CompatFileProvider : FileProvider() {
    
    companion object {
        fun getUriForFile(context: Context, file: File): Uri {
            return getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        }
        
        fun getUriForFile(context: Context, authority: String, file: File): Uri {
            return FileProvider.getUriForFile(context, authority, file)
        }
    }
}
