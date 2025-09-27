package com.moderncalendar.util

import androidx.core.content.FileProvider

class CompatFileProvider : FileProvider() {
    // This class provides file sharing capabilities for calendar files
    // It extends FileProvider to handle file URIs securely
}
