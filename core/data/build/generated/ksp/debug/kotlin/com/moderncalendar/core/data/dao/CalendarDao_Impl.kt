package com.moderncalendar.core.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.moderncalendar.core.`data`.converter.DateTimeConverter
import com.moderncalendar.core.`data`.entity.CalendarEntity
import com.moderncalendar.core.`data`.entity.CalendarType
import java.time.LocalDateTime
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.IllegalArgumentException
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
public class CalendarDao_Impl(
  __db: RoomDatabase,
) : CalendarDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCalendarEntity: EntityInsertAdapter<CalendarEntity>

  private val __dateTimeConverter: DateTimeConverter = DateTimeConverter()

  private val __updateAdapterOfCalendarEntity: EntityDeleteOrUpdateAdapter<CalendarEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfCalendarEntity = object : EntityInsertAdapter<CalendarEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `calendars` (`id`,`name`,`description`,`color`,`is_visible`,`is_primary`,`calendar_type`,`account_email`,`sync_id`,`is_synced`,`created_at`,`updated_at`,`is_deleted`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CalendarEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        statement.bindText(4, entity.color)
        val _tmp: Int = if (entity.isVisible) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        val _tmp_1: Int = if (entity.isPrimary) 1 else 0
        statement.bindLong(6, _tmp_1.toLong())
        statement.bindText(7, __CalendarType_enumToString(entity.calendarType))
        val _tmpAccountEmail: String? = entity.accountEmail
        if (_tmpAccountEmail == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpAccountEmail)
        }
        val _tmpSyncId: String? = entity.syncId
        if (_tmpSyncId == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpSyncId)
        }
        val _tmp_2: Int = if (entity.isSynced) 1 else 0
        statement.bindLong(10, _tmp_2.toLong())
        val _tmp_3: String? = __dateTimeConverter.fromLocalDateTime(entity.createdAt)
        if (_tmp_3 == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmp_3)
        }
        val _tmp_4: String? = __dateTimeConverter.fromLocalDateTime(entity.updatedAt)
        if (_tmp_4 == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmp_4)
        }
        val _tmp_5: Int = if (entity.isDeleted) 1 else 0
        statement.bindLong(13, _tmp_5.toLong())
      }
    }
    this.__updateAdapterOfCalendarEntity = object : EntityDeleteOrUpdateAdapter<CalendarEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `calendars` SET `id` = ?,`name` = ?,`description` = ?,`color` = ?,`is_visible` = ?,`is_primary` = ?,`calendar_type` = ?,`account_email` = ?,`sync_id` = ?,`is_synced` = ?,`created_at` = ?,`updated_at` = ?,`is_deleted` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CalendarEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        statement.bindText(4, entity.color)
        val _tmp: Int = if (entity.isVisible) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        val _tmp_1: Int = if (entity.isPrimary) 1 else 0
        statement.bindLong(6, _tmp_1.toLong())
        statement.bindText(7, __CalendarType_enumToString(entity.calendarType))
        val _tmpAccountEmail: String? = entity.accountEmail
        if (_tmpAccountEmail == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpAccountEmail)
        }
        val _tmpSyncId: String? = entity.syncId
        if (_tmpSyncId == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpSyncId)
        }
        val _tmp_2: Int = if (entity.isSynced) 1 else 0
        statement.bindLong(10, _tmp_2.toLong())
        val _tmp_3: String? = __dateTimeConverter.fromLocalDateTime(entity.createdAt)
        if (_tmp_3 == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmp_3)
        }
        val _tmp_4: String? = __dateTimeConverter.fromLocalDateTime(entity.updatedAt)
        if (_tmp_4 == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmp_4)
        }
        val _tmp_5: Int = if (entity.isDeleted) 1 else 0
        statement.bindLong(13, _tmp_5.toLong())
        statement.bindText(14, entity.id)
      }
    }
  }

  public override suspend fun insertCalendar(calendar: CalendarEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfCalendarEntity.insert(_connection, calendar)
  }

  public override suspend fun insertCalendars(calendars: List<CalendarEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfCalendarEntity.insert(_connection, calendars)
  }

  public override suspend fun updateCalendar(calendar: CalendarEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfCalendarEntity.handle(_connection, calendar)
  }

  public override fun getAllCalendars(): Flow<List<CalendarEntity>> {
    val _sql: String =
        "SELECT * FROM calendars WHERE is_deleted = 0 ORDER BY is_primary DESC, name ASC"
    return createFlow(__db, false, arrayOf("calendars")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfIsVisible: Int = getColumnIndexOrThrow(_stmt, "is_visible")
        val _cursorIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "is_primary")
        val _cursorIndexOfCalendarType: Int = getColumnIndexOrThrow(_stmt, "calendar_type")
        val _cursorIndexOfAccountEmail: Int = getColumnIndexOrThrow(_stmt, "account_email")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: MutableList<CalendarEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CalendarEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpIsVisible: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_cursorIndexOfIsVisible).toInt()
          _tmpIsVisible = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_cursorIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
          val _tmpCalendarType: CalendarType
          _tmpCalendarType = __CalendarType_stringToEnum(_stmt.getText(_cursorIndexOfCalendarType))
          val _tmpAccountEmail: String?
          if (_stmt.isNull(_cursorIndexOfAccountEmail)) {
            _tmpAccountEmail = null
          } else {
            _tmpAccountEmail = _stmt.getText(_cursorIndexOfAccountEmail)
          }
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpIsSynced: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_2 != 0
          val _tmpCreatedAt: LocalDateTime
          val _tmp_3: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_4: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_4
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_5: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_6: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_6
          }
          val _tmpIsDeleted: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_7 != 0
          _item =
              CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCalendarById(calendarId: String): CalendarEntity? {
    val _sql: String = "SELECT * FROM calendars WHERE id = ? AND is_deleted = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, calendarId)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfIsVisible: Int = getColumnIndexOrThrow(_stmt, "is_visible")
        val _cursorIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "is_primary")
        val _cursorIndexOfCalendarType: Int = getColumnIndexOrThrow(_stmt, "calendar_type")
        val _cursorIndexOfAccountEmail: Int = getColumnIndexOrThrow(_stmt, "account_email")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: CalendarEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpIsVisible: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_cursorIndexOfIsVisible).toInt()
          _tmpIsVisible = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_cursorIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
          val _tmpCalendarType: CalendarType
          _tmpCalendarType = __CalendarType_stringToEnum(_stmt.getText(_cursorIndexOfCalendarType))
          val _tmpAccountEmail: String?
          if (_stmt.isNull(_cursorIndexOfAccountEmail)) {
            _tmpAccountEmail = null
          } else {
            _tmpAccountEmail = _stmt.getText(_cursorIndexOfAccountEmail)
          }
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpIsSynced: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_2 != 0
          val _tmpCreatedAt: LocalDateTime
          val _tmp_3: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_4: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_4
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_5: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_6: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_6
          }
          val _tmpIsDeleted: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_7 != 0
          _result =
              CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getVisibleCalendars(): Flow<List<CalendarEntity>> {
    val _sql: String =
        "SELECT * FROM calendars WHERE is_visible = 1 AND is_deleted = 0 ORDER BY is_primary DESC, name ASC"
    return createFlow(__db, false, arrayOf("calendars")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfIsVisible: Int = getColumnIndexOrThrow(_stmt, "is_visible")
        val _cursorIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "is_primary")
        val _cursorIndexOfCalendarType: Int = getColumnIndexOrThrow(_stmt, "calendar_type")
        val _cursorIndexOfAccountEmail: Int = getColumnIndexOrThrow(_stmt, "account_email")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: MutableList<CalendarEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CalendarEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpIsVisible: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_cursorIndexOfIsVisible).toInt()
          _tmpIsVisible = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_cursorIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
          val _tmpCalendarType: CalendarType
          _tmpCalendarType = __CalendarType_stringToEnum(_stmt.getText(_cursorIndexOfCalendarType))
          val _tmpAccountEmail: String?
          if (_stmt.isNull(_cursorIndexOfAccountEmail)) {
            _tmpAccountEmail = null
          } else {
            _tmpAccountEmail = _stmt.getText(_cursorIndexOfAccountEmail)
          }
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpIsSynced: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_2 != 0
          val _tmpCreatedAt: LocalDateTime
          val _tmp_3: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_4: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_4
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_5: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_6: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_6
          }
          val _tmpIsDeleted: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_7 != 0
          _item =
              CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getCalendarsByType(type: String): Flow<List<CalendarEntity>> {
    val _sql: String =
        "SELECT * FROM calendars WHERE calendar_type = ? AND is_deleted = 0 ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("calendars")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, type)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfIsVisible: Int = getColumnIndexOrThrow(_stmt, "is_visible")
        val _cursorIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "is_primary")
        val _cursorIndexOfCalendarType: Int = getColumnIndexOrThrow(_stmt, "calendar_type")
        val _cursorIndexOfAccountEmail: Int = getColumnIndexOrThrow(_stmt, "account_email")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: MutableList<CalendarEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CalendarEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpIsVisible: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_cursorIndexOfIsVisible).toInt()
          _tmpIsVisible = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_cursorIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
          val _tmpCalendarType: CalendarType
          _tmpCalendarType = __CalendarType_stringToEnum(_stmt.getText(_cursorIndexOfCalendarType))
          val _tmpAccountEmail: String?
          if (_stmt.isNull(_cursorIndexOfAccountEmail)) {
            _tmpAccountEmail = null
          } else {
            _tmpAccountEmail = _stmt.getText(_cursorIndexOfAccountEmail)
          }
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpIsSynced: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_2 != 0
          val _tmpCreatedAt: LocalDateTime
          val _tmp_3: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_4: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_4
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_5: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_6: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_6
          }
          val _tmpIsDeleted: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_7 != 0
          _item =
              CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getPrimaryCalendar(): CalendarEntity? {
    val _sql: String = "SELECT * FROM calendars WHERE is_primary = 1 AND is_deleted = 0 LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfIsVisible: Int = getColumnIndexOrThrow(_stmt, "is_visible")
        val _cursorIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "is_primary")
        val _cursorIndexOfCalendarType: Int = getColumnIndexOrThrow(_stmt, "calendar_type")
        val _cursorIndexOfAccountEmail: Int = getColumnIndexOrThrow(_stmt, "account_email")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: CalendarEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpIsVisible: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_cursorIndexOfIsVisible).toInt()
          _tmpIsVisible = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_cursorIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
          val _tmpCalendarType: CalendarType
          _tmpCalendarType = __CalendarType_stringToEnum(_stmt.getText(_cursorIndexOfCalendarType))
          val _tmpAccountEmail: String?
          if (_stmt.isNull(_cursorIndexOfAccountEmail)) {
            _tmpAccountEmail = null
          } else {
            _tmpAccountEmail = _stmt.getText(_cursorIndexOfAccountEmail)
          }
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpIsSynced: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_2 != 0
          val _tmpCreatedAt: LocalDateTime
          val _tmp_3: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_4: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_4
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_5: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_6: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_6
          }
          val _tmpIsDeleted: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_7 != 0
          _result =
              CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCalendarBySyncId(syncId: String): CalendarEntity? {
    val _sql: String = "SELECT * FROM calendars WHERE sync_id = ? AND is_deleted = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, syncId)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfIsVisible: Int = getColumnIndexOrThrow(_stmt, "is_visible")
        val _cursorIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "is_primary")
        val _cursorIndexOfCalendarType: Int = getColumnIndexOrThrow(_stmt, "calendar_type")
        val _cursorIndexOfAccountEmail: Int = getColumnIndexOrThrow(_stmt, "account_email")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: CalendarEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpIsVisible: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_cursorIndexOfIsVisible).toInt()
          _tmpIsVisible = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_cursorIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
          val _tmpCalendarType: CalendarType
          _tmpCalendarType = __CalendarType_stringToEnum(_stmt.getText(_cursorIndexOfCalendarType))
          val _tmpAccountEmail: String?
          if (_stmt.isNull(_cursorIndexOfAccountEmail)) {
            _tmpAccountEmail = null
          } else {
            _tmpAccountEmail = _stmt.getText(_cursorIndexOfAccountEmail)
          }
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpIsSynced: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_2 != 0
          val _tmpCreatedAt: LocalDateTime
          val _tmp_3: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_4: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_4
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_5: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_6: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_6
          }
          val _tmpIsDeleted: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_7 != 0
          _result =
              CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getUnsyncedCalendars(): List<CalendarEntity> {
    val _sql: String = "SELECT * FROM calendars WHERE is_synced = 0 AND is_deleted = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _cursorIndexOfIsVisible: Int = getColumnIndexOrThrow(_stmt, "is_visible")
        val _cursorIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "is_primary")
        val _cursorIndexOfCalendarType: Int = getColumnIndexOrThrow(_stmt, "calendar_type")
        val _cursorIndexOfAccountEmail: Int = getColumnIndexOrThrow(_stmt, "account_email")
        val _cursorIndexOfSyncId: Int = getColumnIndexOrThrow(_stmt, "sync_id")
        val _cursorIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "is_synced")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updated_at")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "is_deleted")
        val _result: MutableList<CalendarEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CalendarEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_cursorIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          }
          val _tmpColor: String
          _tmpColor = _stmt.getText(_cursorIndexOfColor)
          val _tmpIsVisible: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_cursorIndexOfIsVisible).toInt()
          _tmpIsVisible = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_cursorIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
          val _tmpCalendarType: CalendarType
          _tmpCalendarType = __CalendarType_stringToEnum(_stmt.getText(_cursorIndexOfCalendarType))
          val _tmpAccountEmail: String?
          if (_stmt.isNull(_cursorIndexOfAccountEmail)) {
            _tmpAccountEmail = null
          } else {
            _tmpAccountEmail = _stmt.getText(_cursorIndexOfAccountEmail)
          }
          val _tmpSyncId: String?
          if (_stmt.isNull(_cursorIndexOfSyncId)) {
            _tmpSyncId = null
          } else {
            _tmpSyncId = _stmt.getText(_cursorIndexOfSyncId)
          }
          val _tmpIsSynced: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_cursorIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp_2 != 0
          val _tmpCreatedAt: LocalDateTime
          val _tmp_3: String?
          if (_stmt.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_cursorIndexOfCreatedAt)
          }
          val _tmp_4: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_4
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_5: String?
          if (_stmt.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_cursorIndexOfUpdatedAt)
          }
          val _tmp_6: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_6
          }
          val _tmpIsDeleted: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp_7 != 0
          _item =
              CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteCalendar(calendarId: String, updatedAt: LocalDateTime) {
    val _sql: String = "UPDATE calendars SET is_deleted = 1, updated_at = ? WHERE id = ?"
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
        _stmt.bindText(_argIndex, calendarId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun unsetPrimaryCalendar(calendarId: String) {
    val _sql: String = "UPDATE calendars SET is_primary = 0 WHERE id != ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, calendarId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun setPrimaryCalendar(calendarId: String) {
    val _sql: String = "UPDATE calendars SET is_primary = 1 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, calendarId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateCalendarVisibility(calendarId: String, isVisible: Boolean) {
    val _sql: String = "UPDATE calendars SET is_visible = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Int = if (isVisible) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _argIndex = 2
        _stmt.bindText(_argIndex, calendarId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markCalendarAsSynced(calendarId: String) {
    val _sql: String = "UPDATE calendars SET is_synced = 1 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, calendarId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markCalendarAsUnsynced(syncId: String) {
    val _sql: String = "UPDATE calendars SET is_synced = 0 WHERE sync_id = ?"
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

  private fun __CalendarType_enumToString(_value: CalendarType): String = when (_value) {
    CalendarType.LOCAL -> "LOCAL"
    CalendarType.GOOGLE -> "GOOGLE"
    CalendarType.MICROSOFT -> "MICROSOFT"
    CalendarType.CALDAV -> "CALDAV"
  }

  private fun __CalendarType_stringToEnum(_value: String): CalendarType = when (_value) {
    "LOCAL" -> CalendarType.LOCAL
    "GOOGLE" -> CalendarType.GOOGLE
    "MICROSOFT" -> CalendarType.MICROSOFT
    "CALDAV" -> CalendarType.CALDAV
    else -> throw IllegalArgumentException("Can't convert value to enum, unknown value: " + _value)
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
