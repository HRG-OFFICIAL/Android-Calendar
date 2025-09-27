package com.moderncalendar.core.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {
    
    private val gson = Gson()
    
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.let { gson.toJson(it) }
    }
    
    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return value?.let { 
            val listType = object : TypeToken<List<Int>>() {}.type
            gson.fromJson(it, listType)
        }
    }
}
