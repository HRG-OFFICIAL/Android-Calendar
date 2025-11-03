package com.moderncalendar.core.data.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Room type converters for complex data types
 */
class Converters {
    
    private val gson = Gson()
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(dateTimeFormatter)
    }
    
    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let { LocalDateTime.parse(it, dateTimeFormatter) }
    }
    
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toStringList(value: String?): List<String> {
        return if (value.isNullOrEmpty()) {
            emptyList()
        } else {
            val listType = object : TypeToken<List<String>>() {}.type
            gson.fromJson(value, listType) ?: emptyList()
        }
    }
    
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toIntList(value: String?): List<Int> {
        return if (value.isNullOrEmpty()) {
            emptyList()
        } else {
            val listType = object : TypeToken<List<Int>>() {}.type
            gson.fromJson(value, listType) ?: emptyList()
        }
    }
    
    @TypeConverter
    fun fromEventPriority(priority: EventPriority?): String? {
        return priority?.name
    }
    
    @TypeConverter
    fun toEventPriority(priorityString: String?): EventPriority? {
        return priorityString?.let { EventPriority.valueOf(it) }
    }
}