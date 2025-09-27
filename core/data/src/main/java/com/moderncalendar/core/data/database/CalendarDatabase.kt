package com.moderncalendar.core.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.moderncalendar.core.data.converter.DateTimeConverter
import com.moderncalendar.core.data.converter.ListConverter
import com.moderncalendar.core.data.converter.RecurrenceConverter
import com.moderncalendar.core.data.dao.CalendarDao
import com.moderncalendar.core.data.dao.EventDao
import com.moderncalendar.core.data.entity.CalendarEntity
import com.moderncalendar.core.data.entity.EventEntity

@Database(
    entities = [EventEntity::class, CalendarEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateTimeConverter::class,
    RecurrenceConverter::class,
    ListConverter::class
)
abstract class CalendarDatabase : RoomDatabase() {
    
    abstract fun eventDao(): EventDao
    abstract fun calendarDao(): CalendarDao
    
    companion object {
        const val DATABASE_NAME = "calendar_database"
        
        @Volatile
        private var INSTANCE: CalendarDatabase? = null
        
        fun getDatabase(context: Context): CalendarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalendarDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
