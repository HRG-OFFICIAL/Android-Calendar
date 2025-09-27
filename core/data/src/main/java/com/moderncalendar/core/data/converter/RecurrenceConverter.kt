package com.moderncalendar.core.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moderncalendar.core.data.entity.RecurrenceRule

class RecurrenceConverter {
    
    private val gson = Gson()
    
    @TypeConverter
    fun fromRecurrenceRule(recurrenceRule: RecurrenceRule?): String? {
        return recurrenceRule?.let { gson.toJson(it) }
    }
    
    @TypeConverter
    fun toRecurrenceRule(recurrenceRuleString: String?): RecurrenceRule? {
        return recurrenceRuleString?.let { 
            gson.fromJson(it, RecurrenceRule::class.java)
        }
    }
}
