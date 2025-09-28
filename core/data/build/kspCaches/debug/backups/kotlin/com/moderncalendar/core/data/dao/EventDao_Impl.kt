package com.moderncalendar.core.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.moderncalendar.core.`data`.converter.DateTimeConverter
import com.moderncalendar.core.`data`.entity.EventEntity
import java.time.LocalDateTime
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class EventDao_Impl(
  __db: RoomDatabase,
) : EventDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfEventEntity: EntityInsertAdapter<EventEntity>

  private val __dateTimeConverter: DateTimeConverter = DateTimeConverter()

  private val __updateAdapterOfEventEntity: EntityDeleteOrUpdateAdapter<EventEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfEventEntity = object : EntityInsertAdapter<EventEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `events` (`id`,`title`,`description`,`start_date_time`,`end_date_time`,`is_all_day`,`location`,`color`,`calendar_id`,`recurrence_rule`,`reminder_minutes`,`is_synced`,`sync_id`,`created_at`,`updated_at`,`is_deleted`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: EventEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        val _tmp: String? = __dateTimeConverter.fromLocalDateTime(entity.startDateTime)
        if (_tmp == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp)
        }
        val _tmp_1: String? = __dateTimeConverter.fromLocalDateTime(entity.endDateTime)
        if (_tmp_1 == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmp_1)
        }
        val _tmp_2: Int = if (entity.isAllDay) 1 else 0
        statement.bindLong(6, _tmp_2.toLong())
        val _tmpLocation: String? = entity.location
        if (_tmpLocation == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpLocation)
        }
        statement.bindText(8, entity.color)
        statement.bindText(9, entity.calendarId)
        val _tmpRecurrenceRule: String? = entity.recurrenceRule
        if (_tmpRecurrenceRule == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpRecurrenceRule)
        }
        statement.bindText(11, entity.reminderMinutes)
        val _tmp_3: Int = if (entity.isSynced) 1 else 0
        statement.bindLong(12, _tmp_3.toLong())
        val _tmpSyncId: String? = entity.syncId
        if (_tmpSyncId == null) {
          statement.bindNull(13)
        } else {
          statement.bindText(13, _tmpSyncId)
        }
        val _tmp_4: String? = __dateTimeConverter.fromLocalDateTime(entity.createdAt)
        if (_tmp_4 == null) {
          statement.bindNull(14)
        } else {
          statement.bindText(14, _tmp_4)
        }
        val _tmp_5: String? = __dateTimeConverter.fromLocalDateTime(entity.updatedAt)
        if (_tmp_5 == null) {
          statement.bindNull(15)
        } else {
          statement.bindText(15, _tmp_5)
        }
        val _tmp_6: Int = if (entity.isDeleted) 1 else 0
        statement.bindLong(16, _tmp_6.toLong())
      }
    }
    this.__updateAdapterOfEventEntity = object : EntityDeleteOrUpdateAdapter<EventEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `events` SET `id` = ?,`title` = ?,`description` = ?,`start_date_time` = ?,`end_date_time` = ?,`is_all_day` = ?,`location` = ?,`color` = ?,`calendar_id` = ?,`recurrence_rule` = ?,`reminder_minutes` = ?,`is_synced` = ?,`sync_id` = ?,`created_at` = ?,`updated_at` = ?,`is_deleted` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: EventEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        val _tmp: String? = __dateTimeConverter.fromLocalDateTime(entity.startDateTime)
        if (_tmp == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp)
        }
        val _tmp_1: String? = __dateTimeConverter.fromLocalDateTime(entity.endDateTime)
        if (_tmp_1 == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmp_1)
        }
        val _tmp_2: Int = if (entity.isAllDay) 1 else 0
        statement.bindLong(6, _tmp_2.toLong())
        val _tmpLocation: String? = entity.location
        if (_tmpLocation == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpLocation)
        }
        statement.bindText(8, entity.color)
        statement.bindText(9, entity.calendarId)
        val _tmpRecurrenceRule: String? = entity.recurrenceRule
        if (_tmpRecurrenceRule == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpRecurrenceRule)
        }
        statement.bindText(11, entity.reminderMinutes)
        val _tmp_3: Int = if (entity.isSynced) 1 else 0
        statement.bindLong(12, _tmp_3.toLong())
        val _tmpSyncId: String? = entity.syncId
        if (_tmpSyncId == null) {
          statement.bindNull(13)
        } else {
          statement.bindText(13, _tmpSyncId)
        }
        val _tmp_4: String? = __dateTimeConverter.fromLocalDateTime(entity.createdAt)
        if (_tmp_4 == null) {
          statement.bindNull(14)
        } else {
          statement.bindText(14, _tmp_4)
        }
        val _tmp_5: String? = __dateTimeConverter.fromLocalDateTime(entity.updatedAt)
        if (_tmp_5 == null) {
          statement.bindNull(15)
        } else {
          statement.bindText(15, _tmp_5)
        }
        val _tmp_6: Int = if (entity.isDeleted) 1 else 0
        statement.bindLong(16, _tmp_6.toLong())
        statement.bindText(17, entity.id)
      }
    }
  }

  public override suspend fun insertEvent(event: EventEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfEventEntity.insert(_connection, event)
  }

  public override suspend fun insertEvents(events: List<EventEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfEventEntity.insert(_connection, events)
  }

  public override suspend fun updateEvent(event: EventEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfEventEntity.handle(_connection, event)
  }

  public override fun getAllEvents(): Flow<List<EventEntity>> {
    val _sql: String = "SELECT * FROM events WHERE is_deleted = 0 ORDER BY start_date_time ASC"
    return createFlow(__db, false, arrayOf("events")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfStartDateTime: Int = getColumnIndexOrThrow(_stmt, "start_date_time")
        val _cursorIndexOfEndDateTime: Int = getColumnIndexOrThrow(_stmt, "end_date_time")
        val _cursorIndexOfIsAllDay: Int = getColumnIndexOrThrow(_stmt, "is_all_day")
        val _cursorIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfCalendarId: Int = getColumnIndexOrThrow(_stmt, "calendar_id")
        val _cursorIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrence_rule")
        val _cursorIndexOfReminderMinutes: Int = getColumnIndexOrThrow(_stmt, "reminder_minutes")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: MutableList<EventEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EventEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_cursorIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpStartDateTime: LocalDateTime
          val _tmp: String?
          if (_stmt.isNull(_cursorIndexOfStartDateTime)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_cursorIndexOfStartDateTime)
          }
          val _tmp_1: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpStartDateTime = _tmp_1
          }
          val _tmpEndDateTime: LocalDateTime
          val _tmp_2: String?
          if (_stmt.isNull(_cursorIndexOfEndDateTime)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_cursorIndexOfEndDateTime)
          }
          val _tmp_3: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpEndDateTime = _tmp_3
          }
          val _tmpIsAllDay: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_cursorIndexOfIsAllDay).toInt()
          _tmpIsAllDay = _tmp_4 != 0
          val _tmpLocation: String?
          if (_stmt.isNull(_cursorIndexOfLocation)) {
            _tmpLocation = null
          } else {
            _tmpLocation = _stmt.getText(_cursorIndexOfLocation)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpCalendarId: String
          _tmpCalendarId = _stmt.getText(_cursorIndexOfCalendarId)
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_cursorIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_cursorIndexOfRecurrenceRule)
          }
          val _tmpReminderMinutes: String
          _tmpReminderMinutes = _stmt.getText(_cursorIndexOfReminderMinutes)
          val _tmpIsSynced: Boolean
          val _tmp_5: Int
          _tmp_5 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_5 != 0
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpCreatedAt: LocalDateTime
          val _tmp_6: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_7: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_7
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_8: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_9: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_9
          }
          val _tmpIsDeleted: Boolean
          val _tmp_10: Int
          _tmp_10 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_10 != 0
          _item =
              EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getEventById(eventId: String): EventEntity? {
    val _sql: String = "SELECT * FROM events WHERE id = ? AND is_deleted = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, eventId)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfStartDateTime: Int = getColumnIndexOrThrow(_stmt, "start_date_time")
        val _cursorIndexOfEndDateTime: Int = getColumnIndexOrThrow(_stmt, "end_date_time")
        val _cursorIndexOfIsAllDay: Int = getColumnIndexOrThrow(_stmt, "is_all_day")
        val _cursorIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfCalendarId: Int = getColumnIndexOrThrow(_stmt, "calendar_id")
        val _cursorIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrence_rule")
        val _cursorIndexOfReminderMinutes: Int = getColumnIndexOrThrow(_stmt, "reminder_minutes")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: EventEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_cursorIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpStartDateTime: LocalDateTime
          val _tmp: String?
          if (_stmt.isNull(_cursorIndexOfStartDateTime)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_cursorIndexOfStartDateTime)
          }
          val _tmp_1: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpStartDateTime = _tmp_1
          }
          val _tmpEndDateTime: LocalDateTime
          val _tmp_2: String?
          if (_stmt.isNull(_cursorIndexOfEndDateTime)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_cursorIndexOfEndDateTime)
          }
          val _tmp_3: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpEndDateTime = _tmp_3
          }
          val _tmpIsAllDay: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_cursorIndexOfIsAllDay).toInt()
          _tmpIsAllDay = _tmp_4 != 0
          val _tmpLocation: String?
          if (_stmt.isNull(_cursorIndexOfLocation)) {
            _tmpLocation = null
          } else {
            _tmpLocation = _stmt.getText(_cursorIndexOfLocation)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpCalendarId: String
          _tmpCalendarId = _stmt.getText(_cursorIndexOfCalendarId)
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_cursorIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_cursorIndexOfRecurrenceRule)
          }
          val _tmpReminderMinutes: String
          _tmpReminderMinutes = _stmt.getText(_cursorIndexOfReminderMinutes)
          val _tmpIsSynced: Boolean
          val _tmp_5: Int
          _tmp_5 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_5 != 0
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpCreatedAt: LocalDateTime
          val _tmp_6: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_7: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_7
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_8: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_9: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_9
          }
          val _tmpIsDeleted: Boolean
          val _tmp_10: Int
          _tmp_10 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_10 != 0
          _result =
              EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getEventsByCalendarId(calendarId: String): Flow<List<EventEntity>> {
    val _sql: String =
        "SELECT * FROM events WHERE calendar_id = ? AND is_deleted = 0 ORDER BY start_date_time ASC"
    return createFlow(__db, false, arrayOf("events")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, calendarId)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfStartDateTime: Int = getColumnIndexOrThrow(_stmt, "start_date_time")
        val _cursorIndexOfEndDateTime: Int = getColumnIndexOrThrow(_stmt, "end_date_time")
        val _cursorIndexOfIsAllDay: Int = getColumnIndexOrThrow(_stmt, "is_all_day")
        val _cursorIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfCalendarId: Int = getColumnIndexOrThrow(_stmt, "calendar_id")
        val _cursorIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrence_rule")
        val _cursorIndexOfReminderMinutes: Int = getColumnIndexOrThrow(_stmt, "reminder_minutes")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: MutableList<EventEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EventEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_cursorIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpStartDateTime: LocalDateTime
          val _tmp: String?
          if (_stmt.isNull(_cursorIndexOfStartDateTime)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_cursorIndexOfStartDateTime)
          }
          val _tmp_1: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpStartDateTime = _tmp_1
          }
          val _tmpEndDateTime: LocalDateTime
          val _tmp_2: String?
          if (_stmt.isNull(_cursorIndexOfEndDateTime)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_cursorIndexOfEndDateTime)
          }
          val _tmp_3: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpEndDateTime = _tmp_3
          }
          val _tmpIsAllDay: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_cursorIndexOfIsAllDay).toInt()
          _tmpIsAllDay = _tmp_4 != 0
          val _tmpLocation: String?
          if (_stmt.isNull(_cursorIndexOfLocation)) {
            _tmpLocation = null
          } else {
            _tmpLocation = _stmt.getText(_cursorIndexOfLocation)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpCalendarId: String
          _tmpCalendarId = _stmt.getText(_cursorIndexOfCalendarId)
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_cursorIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_cursorIndexOfRecurrenceRule)
          }
          val _tmpReminderMinutes: String
          _tmpReminderMinutes = _stmt.getText(_cursorIndexOfReminderMinutes)
          val _tmpIsSynced: Boolean
          val _tmp_5: Int
          _tmp_5 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_5 != 0
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpCreatedAt: LocalDateTime
          val _tmp_6: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_7: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_7
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_8: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_9: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_9
          }
          val _tmpIsDeleted: Boolean
          val _tmp_10: Int
          _tmp_10 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_10 != 0
          _item =
              EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getEventsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime):
      Flow<List<EventEntity>> {
    val _sql: String =
        "SELECT * FROM events WHERE start_date_time >= ? AND start_date_time < ? AND is_deleted = 0 ORDER BY start_date_time ASC"
    return createFlow(__db, false, arrayOf("events")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __dateTimeConverter.fromLocalDateTime(startDate)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        _argIndex = 2
        val _tmp_1: String? = __dateTimeConverter.fromLocalDateTime(endDate)
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp_1)
        }
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfStartDateTime: Int = getColumnIndexOrThrow(_stmt, "start_date_time")
        val _cursorIndexOfEndDateTime: Int = getColumnIndexOrThrow(_stmt, "end_date_time")
        val _cursorIndexOfIsAllDay: Int = getColumnIndexOrThrow(_stmt, "is_all_day")
        val _cursorIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfCalendarId: Int = getColumnIndexOrThrow(_stmt, "calendar_id")
        val _cursorIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrence_rule")
        val _cursorIndexOfReminderMinutes: Int = getColumnIndexOrThrow(_stmt, "reminder_minutes")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: MutableList<EventEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EventEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_cursorIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpStartDateTime: LocalDateTime
          val _tmp_2: String?
          if (_stmt.isNull(_cursorIndexOfStartDateTime)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_cursorIndexOfStartDateTime)
          }
          val _tmp_3: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpStartDateTime = _tmp_3
          }
          val _tmpEndDateTime: LocalDateTime
          val _tmp_4: String?
          if (_stmt.isNull(_cursorIndexOfEndDateTime)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_cursorIndexOfEndDateTime)
          }
          val _tmp_5: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpEndDateTime = _tmp_5
          }
          val _tmpIsAllDay: Boolean
          val _tmp_6: Int
          _tmp_6 = _stmt.getLong(_cursorIndexOfIsAllDay).toInt()
          _tmpIsAllDay = _tmp_6 != 0
          val _tmpLocation: String?
          if (_stmt.isNull(_cursorIndexOfLocation)) {
            _tmpLocation = null
          } else {
            _tmpLocation = _stmt.getText(_cursorIndexOfLocation)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpCalendarId: String
          _tmpCalendarId = _stmt.getText(_cursorIndexOfCalendarId)
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_cursorIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_cursorIndexOfRecurrenceRule)
          }
          val _tmpReminderMinutes: String
          _tmpReminderMinutes = _stmt.getText(_cursorIndexOfReminderMinutes)
          val _tmpIsSynced: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_7 != 0
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpCreatedAt: LocalDateTime
          val _tmp_8: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_9: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_9
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_10: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_11: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_11
          }
          val _tmpIsDeleted: Boolean
          val _tmp_12: Int
          _tmp_12 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_12 != 0
          _item =
              EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getEventsByDate(startOfDay: LocalDateTime, endOfDay: LocalDateTime):
      Flow<List<EventEntity>> {
    val _sql: String =
        "SELECT * FROM events WHERE start_date_time >= ? AND start_date_time < ? AND is_deleted = 0 ORDER BY start_date_time ASC"
    return createFlow(__db, false, arrayOf("events")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __dateTimeConverter.fromLocalDateTime(startOfDay)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        _argIndex = 2
        val _tmp_1: String? = __dateTimeConverter.fromLocalDateTime(endOfDay)
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp_1)
        }
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfStartDateTime: Int = getColumnIndexOrThrow(_stmt, "start_date_time")
        val _cursorIndexOfEndDateTime: Int = getColumnIndexOrThrow(_stmt, "end_date_time")
        val _cursorIndexOfIsAllDay: Int = getColumnIndexOrThrow(_stmt, "is_all_day")
        val _cursorIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfCalendarId: Int = getColumnIndexOrThrow(_stmt, "calendar_id")
        val _cursorIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrence_rule")
        val _cursorIndexOfReminderMinutes: Int = getColumnIndexOrThrow(_stmt, "reminder_minutes")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: MutableList<EventEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EventEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_cursorIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpStartDateTime: LocalDateTime
          val _tmp_2: String?
          if (_stmt.isNull(_cursorIndexOfStartDateTime)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_cursorIndexOfStartDateTime)
          }
          val _tmp_3: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpStartDateTime = _tmp_3
          }
          val _tmpEndDateTime: LocalDateTime
          val _tmp_4: String?
          if (_stmt.isNull(_cursorIndexOfEndDateTime)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_cursorIndexOfEndDateTime)
          }
          val _tmp_5: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpEndDateTime = _tmp_5
          }
          val _tmpIsAllDay: Boolean
          val _tmp_6: Int
          _tmp_6 = _stmt.getLong(_cursorIndexOfIsAllDay).toInt()
          _tmpIsAllDay = _tmp_6 != 0
          val _tmpLocation: String?
          if (_stmt.isNull(_cursorIndexOfLocation)) {
            _tmpLocation = null
          } else {
            _tmpLocation = _stmt.getText(_cursorIndexOfLocation)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpCalendarId: String
          _tmpCalendarId = _stmt.getText(_cursorIndexOfCalendarId)
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_cursorIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_cursorIndexOfRecurrenceRule)
          }
          val _tmpReminderMinutes: String
          _tmpReminderMinutes = _stmt.getText(_cursorIndexOfReminderMinutes)
          val _tmpIsSynced: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_7 != 0
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpCreatedAt: LocalDateTime
          val _tmp_8: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_9: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_9
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_10: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_11: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_11
          }
          val _tmpIsDeleted: Boolean
          val _tmp_12: Int
          _tmp_12 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_12 != 0
          _item =
              EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun searchEvents(query: String): Flow<List<EventEntity>> {
    val _sql: String =
        "SELECT * FROM events WHERE title LIKE '%' || ? || '%' OR description LIKE '%' || ? || '%' AND is_deleted = 0 ORDER BY start_date_time ASC"
    return createFlow(__db, false, arrayOf("events")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        _argIndex = 2
        _stmt.bindText(_argIndex, query)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfStartDateTime: Int = getColumnIndexOrThrow(_stmt, "start_date_time")
        val _cursorIndexOfEndDateTime: Int = getColumnIndexOrThrow(_stmt, "end_date_time")
        val _cursorIndexOfIsAllDay: Int = getColumnIndexOrThrow(_stmt, "is_all_day")
        val _cursorIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfCalendarId: Int = getColumnIndexOrThrow(_stmt, "calendar_id")
        val _cursorIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrence_rule")
        val _cursorIndexOfReminderMinutes: Int = getColumnIndexOrThrow(_stmt, "reminder_minutes")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: MutableList<EventEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EventEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_cursorIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpStartDateTime: LocalDateTime
          val _tmp: String?
          if (_stmt.isNull(_cursorIndexOfStartDateTime)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_cursorIndexOfStartDateTime)
          }
          val _tmp_1: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpStartDateTime = _tmp_1
          }
          val _tmpEndDateTime: LocalDateTime
          val _tmp_2: String?
          if (_stmt.isNull(_cursorIndexOfEndDateTime)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_cursorIndexOfEndDateTime)
          }
          val _tmp_3: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpEndDateTime = _tmp_3
          }
          val _tmpIsAllDay: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_cursorIndexOfIsAllDay).toInt()
          _tmpIsAllDay = _tmp_4 != 0
          val _tmpLocation: String?
          if (_stmt.isNull(_cursorIndexOfLocation)) {
            _tmpLocation = null
          } else {
            _tmpLocation = _stmt.getText(_cursorIndexOfLocation)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpCalendarId: String
          _tmpCalendarId = _stmt.getText(_cursorIndexOfCalendarId)
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_cursorIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_cursorIndexOfRecurrenceRule)
          }
          val _tmpReminderMinutes: String
          _tmpReminderMinutes = _stmt.getText(_cursorIndexOfReminderMinutes)
          val _tmpIsSynced: Boolean
          val _tmp_5: Int
          _tmp_5 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_5 != 0
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpCreatedAt: LocalDateTime
          val _tmp_6: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_7: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_7
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_8: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_9: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_9
          }
          val _tmpIsDeleted: Boolean
          val _tmp_10: Int
          _tmp_10 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_10 != 0
          _item =
              EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getUnsyncedEvents(): List<EventEntity> {
    val _sql: String = "SELECT * FROM events WHERE is_synced = 0 AND is_deleted = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfStartDateTime: Int = getColumnIndexOrThrow(_stmt, "start_date_time")
        val _cursorIndexOfEndDateTime: Int = getColumnIndexOrThrow(_stmt, "end_date_time")
        val _cursorIndexOfIsAllDay: Int = getColumnIndexOrThrow(_stmt, "is_all_day")
        val _cursorIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfCalendarId: Int = getColumnIndexOrThrow(_stmt, "calendar_id")
        val _cursorIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrence_rule")
        val _cursorIndexOfReminderMinutes: Int = getColumnIndexOrThrow(_stmt, "reminder_minutes")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: MutableList<EventEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EventEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_cursorIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpStartDateTime: LocalDateTime
          val _tmp: String?
          if (_stmt.isNull(_cursorIndexOfStartDateTime)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_cursorIndexOfStartDateTime)
          }
          val _tmp_1: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpStartDateTime = _tmp_1
          }
          val _tmpEndDateTime: LocalDateTime
          val _tmp_2: String?
          if (_stmt.isNull(_cursorIndexOfEndDateTime)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_cursorIndexOfEndDateTime)
          }
          val _tmp_3: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpEndDateTime = _tmp_3
          }
          val _tmpIsAllDay: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_cursorIndexOfIsAllDay).toInt()
          _tmpIsAllDay = _tmp_4 != 0
          val _tmpLocation: String?
          if (_stmt.isNull(_cursorIndexOfLocation)) {
            _tmpLocation = null
          } else {
            _tmpLocation = _stmt.getText(_cursorIndexOfLocation)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpCalendarId: String
          _tmpCalendarId = _stmt.getText(_cursorIndexOfCalendarId)
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_cursorIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_cursorIndexOfRecurrenceRule)
          }
          val _tmpReminderMinutes: String
          _tmpReminderMinutes = _stmt.getText(_cursorIndexOfReminderMinutes)
          val _tmpIsSynced: Boolean
          val _tmp_5: Int
          _tmp_5 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_5 != 0
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpCreatedAt: LocalDateTime
          val _tmp_6: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_7: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_7
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_8: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_9: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_9
          }
          val _tmpIsDeleted: Boolean
          val _tmp_10: Int
          _tmp_10 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_10 != 0
          _item =
              EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getEventBySyncId(syncId: String): EventEntity? {
    val _sql: String = "SELECT * FROM events WHERE sync_id = ? AND is_deleted = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, syncId)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfStartDateTime: Int = getColumnIndexOrThrow(_stmt, "start_date_time")
        val _cursorIndexOfEndDateTime: Int = getColumnIndexOrThrow(_stmt, "end_date_time")
        val _cursorIndexOfIsAllDay: Int = getColumnIndexOrThrow(_stmt, "is_all_day")
        val _cursorIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfCalendarId: Int = getColumnIndexOrThrow(_stmt, "calendar_id")
        val _cursorIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrence_rule")
        val _cursorIndexOfReminderMinutes: Int = getColumnIndexOrThrow(_stmt, "reminder_minutes")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: EventEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_cursorIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpStartDateTime: LocalDateTime
          val _tmp: String?
          if (_stmt.isNull(_cursorIndexOfStartDateTime)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_cursorIndexOfStartDateTime)
          }
          val _tmp_1: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpStartDateTime = _tmp_1
          }
          val _tmpEndDateTime: LocalDateTime
          val _tmp_2: String?
          if (_stmt.isNull(_cursorIndexOfEndDateTime)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_cursorIndexOfEndDateTime)
          }
          val _tmp_3: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpEndDateTime = _tmp_3
          }
          val _tmpIsAllDay: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_cursorIndexOfIsAllDay).toInt()
          _tmpIsAllDay = _tmp_4 != 0
          val _tmpLocation: String?
          if (_stmt.isNull(_cursorIndexOfLocation)) {
            _tmpLocation = null
          } else {
            _tmpLocation = _stmt.getText(_cursorIndexOfLocation)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpCalendarId: String
          _tmpCalendarId = _stmt.getText(_cursorIndexOfCalendarId)
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_cursorIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_cursorIndexOfRecurrenceRule)
          }
          val _tmpReminderMinutes: String
          _tmpReminderMinutes = _stmt.getText(_cursorIndexOfReminderMinutes)
          val _tmpIsSynced: Boolean
          val _tmp_5: Int
          _tmp_5 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_5 != 0
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpCreatedAt: LocalDateTime
          val _tmp_6: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_7: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_7
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_8: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_9: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_9
          }
          val _tmpIsDeleted: Boolean
          val _tmp_10: Int
          _tmp_10 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_10 != 0
          _result =
              EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteEvent(eventId: String, updatedAt: LocalDateTime) {
    val _sql: String = "UPDATE events SET is_deleted = 1, updated_at = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __dateTimeConverter.fromLocalDateTime(updatedAt)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        _argIndex = 2
        _stmt.bindText(_argIndex, eventId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteOldDeletedEvents(cutoffDate: LocalDateTime) {
    val _sql: String = "DELETE FROM events WHERE is_deleted = 1 AND updated_at < ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __dateTimeConverter.fromLocalDateTime(cutoffDate)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markEventAsSynced(eventId: String) {
    val _sql: String = "UPDATE events SET is_synced = 1 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, eventId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markEventAsUnsynced(syncId: String) {
    val _sql: String = "UPDATE events SET is_synced = 0 WHERE sync_id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, syncId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
