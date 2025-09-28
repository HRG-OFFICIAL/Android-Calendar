package com.moderncalendar.core.`data`.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.moderncalendar.core.`data`.dao.CalendarDao
import com.moderncalendar.core.`data`.dao.CalendarDao_Impl
import com.moderncalendar.core.`data`.dao.EventDao
import com.moderncalendar.core.`data`.dao.EventDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CalendarDatabase_Impl : CalendarDatabase() {
  private val _eventDao: Lazy<EventDao> = lazy {
    EventDao_Impl(this)
  }


  private val _calendarDao: Lazy<CalendarDao> = lazy {
    CalendarDao_Impl(this)
  }


  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "fccda1ee5f607a2a83d7ee31c5531c9c", "5a2cf9b2246f52df3b46ed4e1df83d58") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `events` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `start_date_time` TEXT NOT NULL, `end_date_time` TEXT NOT NULL, `is_all_day` INTEGER NOT NULL, `location` TEXT, `color` TEXT NOT NULL, `calendar_id` TEXT NOT NULL, `recurrence_rule` TEXT, `reminder_minutes` TEXT NOT NULL, `is_synced` INTEGER NOT NULL, `sync_id` TEXT, `created_at` TEXT NOT NULL, `updated_at` TEXT NOT NULL, `is_deleted` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `calendars` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `color` TEXT NOT NULL, `is_visible` INTEGER NOT NULL, `is_primary` INTEGER NOT NULL, `calendar_type` TEXT NOT NULL, `account_email` TEXT, `sync_id` TEXT, `is_synced` INTEGER NOT NULL, `created_at` TEXT NOT NULL, `updated_at` TEXT NOT NULL, `is_deleted` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'fccda1ee5f607a2a83d7ee31c5531c9c')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `events`")
        connection.execSQL("DROP TABLE IF EXISTS `calendars`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsEvents: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsEvents.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("description", TableInfo.Column("description", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("start_date_time", TableInfo.Column("start_date_time", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("end_date_time", TableInfo.Column("end_date_time", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("is_all_day", TableInfo.Column("is_all_day", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("location", TableInfo.Column("location", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("color", TableInfo.Column("color", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("calendar_id", TableInfo.Column("calendar_id", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("recurrence_rule", TableInfo.Column("recurrence_rule", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("reminder_minutes", TableInfo.Column("reminder_minutes", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("is_synced", TableInfo.Column("is_synced", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("sync_id", TableInfo.Column("sync_id", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("created_at", TableInfo.Column("created_at", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("updated_at", TableInfo.Column("updated_at", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("is_deleted", TableInfo.Column("is_deleted", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysEvents: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesEvents: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoEvents: TableInfo = TableInfo("events", _columnsEvents, _foreignKeysEvents,
            _indicesEvents)
        val _existingEvents: TableInfo = read(connection, "events")
        if (!_infoEvents.equals(_existingEvents)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |events(com.moderncalendar.core.data.entity.EventEntity).
              | Expected:
              |""".trimMargin() + _infoEvents + """
              |
              | Found:
              |""".trimMargin() + _existingEvents)
        }
        val _columnsCalendars: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCalendars.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCalendars.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCalendars.put("description", TableInfo.Column("description", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCalendars.put("color", TableInfo.Column("color", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCalendars.put("is_visible", TableInfo.Column("is_visible", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCalendars.put("is_primary", TableInfo.Column("is_primary", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCalendars.put("calendar_type", TableInfo.Column("calendar_type", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCalendars.put("account_email", TableInfo.Column("account_email", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCalendars.put("sync_id", TableInfo.Column("sync_id", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCalendars.put("is_synced", TableInfo.Column("is_synced", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCalendars.put("created_at", TableInfo.Column("created_at", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCalendars.put("updated_at", TableInfo.Column("updated_at", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCalendars.put("is_deleted", TableInfo.Column("is_deleted", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCalendars: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCalendars: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCalendars: TableInfo = TableInfo("calendars", _columnsCalendars,
            _foreignKeysCalendars, _indicesCalendars)
        val _existingCalendars: TableInfo = read(connection, "calendars")
        if (!_infoCalendars.equals(_existingCalendars)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |calendars(com.moderncalendar.core.data.entity.CalendarEntity).
              | Expected:
              |""".trimMargin() + _infoCalendars + """
              |
              | Found:
              |""".trimMargin() + _existingCalendars)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "events", "calendars")
  }

  public override fun clearAllTables() {
    super.performClear(false, "events", "calendars")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(EventDao::class, EventDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(CalendarDao::class, CalendarDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun eventDao(): EventDao = _eventDao.value

  public override fun calendarDao(): CalendarDao = _calendarDao.value
}
