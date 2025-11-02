package com.moderncalendar.core.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import com.moderncalendar.core.data.dao.EventDao
import com.moderncalendar.core.data.dao.CalendarDao
import com.moderncalendar.core.data.entity.EventEntity
import com.moderncalendar.core.data.entity.CalendarEntity
import com.moderncalendar.core.data.entity.Converters

@Database(
    entities = [EventEntity::class, CalendarEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CalendarDatabase : RoomDatabase() {
    
    abstract fun eventDao(): EventDao
    abstract fun calendarDao(): CalendarDao
    
    companion object {
        @Volatile
        private var INSTANCE: CalendarDatabase? = null
        
        fun getDatabase(context: Context): CalendarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalendarDatabase::class.java,
                    "calendar_database"
                )
                .addMigrations(MIGRATION_1_2) // Add migrations as needed
                .fallbackToDestructiveMigration() // For development only
                .build()
                INSTANCE = instance
                instance
            }
        }
        
        // Migration from version 1 to 2 (example)
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add new columns or tables here
                // database.execSQL("ALTER TABLE events ADD COLUMN new_column TEXT")
            }
        }
    }
}