package com.moderncalendar.core.localization

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalizationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val _currentLanguage = MutableStateFlow(Locale.getDefault().language)
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()
    
    fun setLanguage(languageCode: String) {
        _currentLanguage.value = languageCode
        updateLocale(languageCode)
    }
    
    private fun updateLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
    
    fun getSupportedLanguages(): List<Language> {
        return listOf(
            Language("en", "English", "English"),
            Language("es", "Español", "Spanish"),
            Language("fr", "Français", "French"),
            Language("de", "Deutsch", "German"),
            Language("it", "Italiano", "Italian"),
            Language("pt", "Português", "Portuguese"),
            Language("ru", "Русский", "Russian"),
            Language("ja", "日本語", "Japanese"),
            Language("ko", "한국어", "Korean"),
            Language("zh", "中文", "Chinese"),
            Language("ar", "العربية", "Arabic"),
            Language("hi", "हिन्दी", "Hindi")
        )
    }
    
    fun getCurrentLanguage(): Language {
        return getSupportedLanguages().find { it.code == _currentLanguage.value }
            ?: getSupportedLanguages().first()
    }
}

data class Language(
    val code: String,
    val nativeName: String,
    val englishName: String
)

@Composable
fun LocalizationSettingsScreen(
    localizationManager: LocalizationManager = hiltViewModel<LocalizationViewModel>().localizationManager
) {
    val currentLanguage by localizationManager.currentLanguage.collectAsState()
    val supportedLanguages = localizationManager.getSupportedLanguages()
    
    Column {
        Text(
            text = "Language Settings",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        supportedLanguages.forEach { language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { localizationManager.setLanguage(language.code) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = currentLanguage.code == language.code,
                    onClick = { localizationManager.setLanguage(language.code) }
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Column {
                    Text(
                        text = language.nativeName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = language.englishName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
