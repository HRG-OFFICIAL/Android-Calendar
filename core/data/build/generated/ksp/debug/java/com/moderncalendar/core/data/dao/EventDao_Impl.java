package com.moderncalendar.core.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.moderncalendar.core.data.converter.DateTimeConverter;
import com.moderncalendar.core.data.entity.EventEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class EventDao_Impl implements EventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EventEntity> __insertionAdapterOfEventEntity;

  private final DateTimeConverter __dateTimeConverter = new DateTimeConverter();

  private final EntityDeletionOrUpdateAdapter<EventEntity> __updateAdapterOfEventEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteEvent;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldDeletedEvents;

  private final SharedSQLiteStatement __preparedStmtOfMarkEventAsSynced;

  private final SharedSQLiteStatement __preparedStmtOfMarkEventAsUnsynced;

  public EventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEventEntity = new EntityInsertionAdapter<EventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `events` (`id`,`title`,`description`,`start_date_time`,`end_date_time`,`startTime`,`endTime`,`is_all_day`,`location`,`color`,`calendar_id`,`recurrence_rule`,`reminder_minutes`,`is_synced`,`sync_id`,`created_at`,`updated_at`,`is_deleted`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EventEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        final String _tmp = __dateTimeConverter.fromLocalDateTime(entity.getStartDateTime());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        final String _tmp_1 = __dateTimeConverter.fromLocalDateTime(entity.getEndDateTime());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        final String _tmp_2 = __dateTimeConverter.fromLocalDateTime(entity.getStartTime());
        if (_tmp_2 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_2);
        }
        final String _tmp_3 = __dateTimeConverter.fromLocalDateTime(entity.getEndTime());
        if (_tmp_3 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_3);
        }
        final int _tmp_4 = entity.isAllDay() ? 1 : 0;
        statement.bindLong(8, _tmp_4);
        if (entity.getLocation() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getLocation());
        }
        statement.bindLong(10, entity.getColor());
        statement.bindString(11, entity.getCalendarId());
        if (entity.getRecurrenceRule() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getRecurrenceRule());
        }
        if (entity.getReminderMinutes() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getReminderMinutes());
        }
        final int _tmp_5 = entity.isSynced() ? 1 : 0;
        statement.bindLong(14, _tmp_5);
        if (entity.getSyncId() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getSyncId());
        }
        final String _tmp_6 = __dateTimeConverter.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp_6 == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, _tmp_6);
        }
        final String _tmp_7 = __dateTimeConverter.fromLocalDateTime(entity.getUpdatedAt());
        if (_tmp_7 == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, _tmp_7);
        }
        final int _tmp_8 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(18, _tmp_8);
      }
    };
    this.__updateAdapterOfEventEntity = new EntityDeletionOrUpdateAdapter<EventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `events` SET `id` = ?,`title` = ?,`description` = ?,`start_date_time` = ?,`end_date_time` = ?,`startTime` = ?,`endTime` = ?,`is_all_day` = ?,`location` = ?,`color` = ?,`calendar_id` = ?,`recurrence_rule` = ?,`reminder_minutes` = ?,`is_synced` = ?,`sync_id` = ?,`created_at` = ?,`updated_at` = ?,`is_deleted` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EventEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        final String _tmp = __dateTimeConverter.fromLocalDateTime(entity.getStartDateTime());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        final String _tmp_1 = __dateTimeConverter.fromLocalDateTime(entity.getEndDateTime());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        final String _tmp_2 = __dateTimeConverter.fromLocalDateTime(entity.getStartTime());
        if (_tmp_2 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_2);
        }
        final String _tmp_3 = __dateTimeConverter.fromLocalDateTime(entity.getEndTime());
        if (_tmp_3 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_3);
        }
        final int _tmp_4 = entity.isAllDay() ? 1 : 0;
        statement.bindLong(8, _tmp_4);
        if (entity.getLocation() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getLocation());
        }
        statement.bindLong(10, entity.getColor());
        statement.bindString(11, entity.getCalendarId());
        if (entity.getRecurrenceRule() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getRecurrenceRule());
        }
        if (entity.getReminderMinutes() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getReminderMinutes());
        }
        final int _tmp_5 = entity.isSynced() ? 1 : 0;
        statement.bindLong(14, _tmp_5);
        if (entity.getSyncId() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getSyncId());
        }
        final String _tmp_6 = __dateTimeConverter.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp_6 == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, _tmp_6);
        }
        final String _tmp_7 = __dateTimeConverter.fromLocalDateTime(entity.getUpdatedAt());
        if (_tmp_7 == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, _tmp_7);
        }
        final int _tmp_8 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(18, _tmp_8);
        statement.bindString(19, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteEvent = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE events SET is_deleted = 1, updated_at = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOldDeletedEvents = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM events WHERE is_deleted = 1 AND updated_at < ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkEventAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE events SET is_synced = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkEventAsUnsynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE events SET is_synced = 0 WHERE sync_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertEvent(final EventEntity event, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfEventEntity.insert(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertEvents(final List<EventEntity> events,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfEventEntity.insert(events);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateEvent(final EventEntity event, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfEventEntity.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteEvent(final String eventId, final LocalDateTime updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteEvent.acquire();
        int _argIndex = 1;
        final String _tmp = __dateTimeConverter.fromLocalDateTime(updatedAt);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp);
        }
        _argIndex = 2;
        _stmt.bindString(_argIndex, eventId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteEvent.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldDeletedEvents(final LocalDateTime cutoffDate,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldDeletedEvents.acquire();
        int _argIndex = 1;
        final String _tmp = __dateTimeConverter.fromLocalDateTime(cutoffDate);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOldDeletedEvents.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markEventAsSynced(final String eventId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkEventAsSynced.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, eventId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkEventAsSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markEventAsUnsynced(final String syncId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkEventAsUnsynced.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, syncId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkEventAsUnsynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<EventEntity>> getAllEvents() {
    final String _sql = "SELECT * FROM events WHERE is_deleted = 0 ORDER BY start_date_time ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"events"}, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date_time");
          final int _cursorIndexOfEndDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date_time");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "is_all_day");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_id");
          final int _cursorIndexOfRecurrenceRule = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence_rule");
          final int _cursorIndexOfReminderMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_minutes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final LocalDateTime _tmpStartDateTime;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDateTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStartDateTime);
            }
            final LocalDateTime _tmp_1 = __dateTimeConverter.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartDateTime = _tmp_1;
            }
            final LocalDateTime _tmpEndDateTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndDateTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEndDateTime);
            }
            final LocalDateTime _tmp_3 = __dateTimeConverter.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndDateTime = _tmp_3;
            }
            final LocalDateTime _tmpStartTime;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalDateTime _tmp_5 = __dateTimeConverter.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_5;
            }
            final LocalDateTime _tmpEndTime;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalDateTime _tmp_7 = __dateTimeConverter.toLocalDateTime(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_7;
            }
            final boolean _tmpIsAllDay;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsAllDay);
            _tmpIsAllDay = _tmp_8 != 0;
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRecurrenceRule;
            if (_cursor.isNull(_cursorIndexOfRecurrenceRule)) {
              _tmpRecurrenceRule = null;
            } else {
              _tmpRecurrenceRule = _cursor.getString(_cursorIndexOfRecurrenceRule);
            }
            final Integer _tmpReminderMinutes;
            if (_cursor.isNull(_cursorIndexOfReminderMinutes)) {
              _tmpReminderMinutes = null;
            } else {
              _tmpReminderMinutes = _cursor.getInt(_cursorIndexOfReminderMinutes);
            }
            final boolean _tmpIsSynced;
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_9 != 0;
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_10;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_10 = null;
            } else {
              _tmp_10 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_11 = __dateTimeConverter.toLocalDateTime(_tmp_10);
            if (_tmp_11 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_11;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_12;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_12 = null;
            } else {
              _tmp_12 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_13 = __dateTimeConverter.toLocalDateTime(_tmp_12);
            if (_tmp_13 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_13;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_14;
            _tmp_14 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_14 != 0;
            _item = new EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpStartTime,_tmpEndTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getEventById(final String eventId,
      final Continuation<? super EventEntity> $completion) {
    final String _sql = "SELECT * FROM events WHERE id = ? AND is_deleted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, eventId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EventEntity>() {
      @Override
      @Nullable
      public EventEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date_time");
          final int _cursorIndexOfEndDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date_time");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "is_all_day");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_id");
          final int _cursorIndexOfRecurrenceRule = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence_rule");
          final int _cursorIndexOfReminderMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_minutes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final EventEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final LocalDateTime _tmpStartDateTime;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDateTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStartDateTime);
            }
            final LocalDateTime _tmp_1 = __dateTimeConverter.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartDateTime = _tmp_1;
            }
            final LocalDateTime _tmpEndDateTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndDateTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEndDateTime);
            }
            final LocalDateTime _tmp_3 = __dateTimeConverter.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndDateTime = _tmp_3;
            }
            final LocalDateTime _tmpStartTime;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalDateTime _tmp_5 = __dateTimeConverter.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_5;
            }
            final LocalDateTime _tmpEndTime;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalDateTime _tmp_7 = __dateTimeConverter.toLocalDateTime(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_7;
            }
            final boolean _tmpIsAllDay;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsAllDay);
            _tmpIsAllDay = _tmp_8 != 0;
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRecurrenceRule;
            if (_cursor.isNull(_cursorIndexOfRecurrenceRule)) {
              _tmpRecurrenceRule = null;
            } else {
              _tmpRecurrenceRule = _cursor.getString(_cursorIndexOfRecurrenceRule);
            }
            final Integer _tmpReminderMinutes;
            if (_cursor.isNull(_cursorIndexOfReminderMinutes)) {
              _tmpReminderMinutes = null;
            } else {
              _tmpReminderMinutes = _cursor.getInt(_cursorIndexOfReminderMinutes);
            }
            final boolean _tmpIsSynced;
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_9 != 0;
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_10;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_10 = null;
            } else {
              _tmp_10 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_11 = __dateTimeConverter.toLocalDateTime(_tmp_10);
            if (_tmp_11 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_11;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_12;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_12 = null;
            } else {
              _tmp_12 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_13 = __dateTimeConverter.toLocalDateTime(_tmp_12);
            if (_tmp_13 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_13;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_14;
            _tmp_14 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_14 != 0;
            _result = new EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpStartTime,_tmpEndTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<EventEntity>> getEventsByCalendarId(final String calendarId) {
    final String _sql = "SELECT * FROM events WHERE calendar_id = ? AND is_deleted = 0 ORDER BY start_date_time ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, calendarId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"events"}, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date_time");
          final int _cursorIndexOfEndDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date_time");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "is_all_day");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_id");
          final int _cursorIndexOfRecurrenceRule = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence_rule");
          final int _cursorIndexOfReminderMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_minutes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final LocalDateTime _tmpStartDateTime;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDateTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStartDateTime);
            }
            final LocalDateTime _tmp_1 = __dateTimeConverter.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartDateTime = _tmp_1;
            }
            final LocalDateTime _tmpEndDateTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndDateTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEndDateTime);
            }
            final LocalDateTime _tmp_3 = __dateTimeConverter.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndDateTime = _tmp_3;
            }
            final LocalDateTime _tmpStartTime;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalDateTime _tmp_5 = __dateTimeConverter.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_5;
            }
            final LocalDateTime _tmpEndTime;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalDateTime _tmp_7 = __dateTimeConverter.toLocalDateTime(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_7;
            }
            final boolean _tmpIsAllDay;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsAllDay);
            _tmpIsAllDay = _tmp_8 != 0;
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRecurrenceRule;
            if (_cursor.isNull(_cursorIndexOfRecurrenceRule)) {
              _tmpRecurrenceRule = null;
            } else {
              _tmpRecurrenceRule = _cursor.getString(_cursorIndexOfRecurrenceRule);
            }
            final Integer _tmpReminderMinutes;
            if (_cursor.isNull(_cursorIndexOfReminderMinutes)) {
              _tmpReminderMinutes = null;
            } else {
              _tmpReminderMinutes = _cursor.getInt(_cursorIndexOfReminderMinutes);
            }
            final boolean _tmpIsSynced;
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_9 != 0;
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_10;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_10 = null;
            } else {
              _tmp_10 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_11 = __dateTimeConverter.toLocalDateTime(_tmp_10);
            if (_tmp_11 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_11;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_12;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_12 = null;
            } else {
              _tmp_12 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_13 = __dateTimeConverter.toLocalDateTime(_tmp_12);
            if (_tmp_13 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_13;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_14;
            _tmp_14 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_14 != 0;
            _item = new EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpStartTime,_tmpEndTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<EventEntity>> getEventsByDateRange(final LocalDateTime startDate,
      final LocalDateTime endDate) {
    final String _sql = "SELECT * FROM events WHERE start_date_time >= ? AND start_date_time < ? AND is_deleted = 0 ORDER BY start_date_time ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __dateTimeConverter.fromLocalDateTime(startDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __dateTimeConverter.fromLocalDateTime(endDate);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"events"}, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date_time");
          final int _cursorIndexOfEndDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date_time");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "is_all_day");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_id");
          final int _cursorIndexOfRecurrenceRule = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence_rule");
          final int _cursorIndexOfReminderMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_minutes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final LocalDateTime _tmpStartDateTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStartDateTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStartDateTime);
            }
            final LocalDateTime _tmp_3 = __dateTimeConverter.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartDateTime = _tmp_3;
            }
            final LocalDateTime _tmpEndDateTime;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfEndDateTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfEndDateTime);
            }
            final LocalDateTime _tmp_5 = __dateTimeConverter.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndDateTime = _tmp_5;
            }
            final LocalDateTime _tmpStartTime;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalDateTime _tmp_7 = __dateTimeConverter.toLocalDateTime(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_7;
            }
            final LocalDateTime _tmpEndTime;
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalDateTime _tmp_9 = __dateTimeConverter.toLocalDateTime(_tmp_8);
            if (_tmp_9 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_9;
            }
            final boolean _tmpIsAllDay;
            final int _tmp_10;
            _tmp_10 = _cursor.getInt(_cursorIndexOfIsAllDay);
            _tmpIsAllDay = _tmp_10 != 0;
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRecurrenceRule;
            if (_cursor.isNull(_cursorIndexOfRecurrenceRule)) {
              _tmpRecurrenceRule = null;
            } else {
              _tmpRecurrenceRule = _cursor.getString(_cursorIndexOfRecurrenceRule);
            }
            final Integer _tmpReminderMinutes;
            if (_cursor.isNull(_cursorIndexOfReminderMinutes)) {
              _tmpReminderMinutes = null;
            } else {
              _tmpReminderMinutes = _cursor.getInt(_cursorIndexOfReminderMinutes);
            }
            final boolean _tmpIsSynced;
            final int _tmp_11;
            _tmp_11 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_11 != 0;
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_12;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_12 = null;
            } else {
              _tmp_12 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_13 = __dateTimeConverter.toLocalDateTime(_tmp_12);
            if (_tmp_13 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_13;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_14;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_14 = null;
            } else {
              _tmp_14 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_15 = __dateTimeConverter.toLocalDateTime(_tmp_14);
            if (_tmp_15 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_15;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_16;
            _tmp_16 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_16 != 0;
            _item = new EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpStartTime,_tmpEndTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<EventEntity>> getEventsByDate(final LocalDateTime startOfDay,
      final LocalDateTime endOfDay) {
    final String _sql = "SELECT * FROM events WHERE start_date_time >= ? AND start_date_time < ? AND is_deleted = 0 ORDER BY start_date_time ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __dateTimeConverter.fromLocalDateTime(startOfDay);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __dateTimeConverter.fromLocalDateTime(endOfDay);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"events"}, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date_time");
          final int _cursorIndexOfEndDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date_time");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "is_all_day");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_id");
          final int _cursorIndexOfRecurrenceRule = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence_rule");
          final int _cursorIndexOfReminderMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_minutes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final LocalDateTime _tmpStartDateTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStartDateTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStartDateTime);
            }
            final LocalDateTime _tmp_3 = __dateTimeConverter.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartDateTime = _tmp_3;
            }
            final LocalDateTime _tmpEndDateTime;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfEndDateTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfEndDateTime);
            }
            final LocalDateTime _tmp_5 = __dateTimeConverter.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndDateTime = _tmp_5;
            }
            final LocalDateTime _tmpStartTime;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalDateTime _tmp_7 = __dateTimeConverter.toLocalDateTime(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_7;
            }
            final LocalDateTime _tmpEndTime;
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalDateTime _tmp_9 = __dateTimeConverter.toLocalDateTime(_tmp_8);
            if (_tmp_9 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_9;
            }
            final boolean _tmpIsAllDay;
            final int _tmp_10;
            _tmp_10 = _cursor.getInt(_cursorIndexOfIsAllDay);
            _tmpIsAllDay = _tmp_10 != 0;
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRecurrenceRule;
            if (_cursor.isNull(_cursorIndexOfRecurrenceRule)) {
              _tmpRecurrenceRule = null;
            } else {
              _tmpRecurrenceRule = _cursor.getString(_cursorIndexOfRecurrenceRule);
            }
            final Integer _tmpReminderMinutes;
            if (_cursor.isNull(_cursorIndexOfReminderMinutes)) {
              _tmpReminderMinutes = null;
            } else {
              _tmpReminderMinutes = _cursor.getInt(_cursorIndexOfReminderMinutes);
            }
            final boolean _tmpIsSynced;
            final int _tmp_11;
            _tmp_11 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_11 != 0;
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_12;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_12 = null;
            } else {
              _tmp_12 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_13 = __dateTimeConverter.toLocalDateTime(_tmp_12);
            if (_tmp_13 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_13;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_14;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_14 = null;
            } else {
              _tmp_14 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_15 = __dateTimeConverter.toLocalDateTime(_tmp_14);
            if (_tmp_15 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_15;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_16;
            _tmp_16 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_16 != 0;
            _item = new EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpStartTime,_tmpEndTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<EventEntity>> searchEvents(final String query) {
    final String _sql = "SELECT * FROM events WHERE title LIKE '%' || ? || '%' OR description LIKE '%' || ? || '%' AND is_deleted = 0 ORDER BY start_date_time ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"events"}, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date_time");
          final int _cursorIndexOfEndDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date_time");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "is_all_day");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_id");
          final int _cursorIndexOfRecurrenceRule = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence_rule");
          final int _cursorIndexOfReminderMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_minutes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final LocalDateTime _tmpStartDateTime;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDateTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStartDateTime);
            }
            final LocalDateTime _tmp_1 = __dateTimeConverter.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartDateTime = _tmp_1;
            }
            final LocalDateTime _tmpEndDateTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndDateTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEndDateTime);
            }
            final LocalDateTime _tmp_3 = __dateTimeConverter.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndDateTime = _tmp_3;
            }
            final LocalDateTime _tmpStartTime;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalDateTime _tmp_5 = __dateTimeConverter.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_5;
            }
            final LocalDateTime _tmpEndTime;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalDateTime _tmp_7 = __dateTimeConverter.toLocalDateTime(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_7;
            }
            final boolean _tmpIsAllDay;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsAllDay);
            _tmpIsAllDay = _tmp_8 != 0;
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRecurrenceRule;
            if (_cursor.isNull(_cursorIndexOfRecurrenceRule)) {
              _tmpRecurrenceRule = null;
            } else {
              _tmpRecurrenceRule = _cursor.getString(_cursorIndexOfRecurrenceRule);
            }
            final Integer _tmpReminderMinutes;
            if (_cursor.isNull(_cursorIndexOfReminderMinutes)) {
              _tmpReminderMinutes = null;
            } else {
              _tmpReminderMinutes = _cursor.getInt(_cursorIndexOfReminderMinutes);
            }
            final boolean _tmpIsSynced;
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_9 != 0;
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_10;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_10 = null;
            } else {
              _tmp_10 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_11 = __dateTimeConverter.toLocalDateTime(_tmp_10);
            if (_tmp_11 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_11;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_12;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_12 = null;
            } else {
              _tmp_12 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_13 = __dateTimeConverter.toLocalDateTime(_tmp_12);
            if (_tmp_13 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_13;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_14;
            _tmp_14 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_14 != 0;
            _item = new EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpStartTime,_tmpEndTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUnsyncedEvents(final Continuation<? super List<EventEntity>> $completion) {
    final String _sql = "SELECT * FROM events WHERE is_synced = 0 AND is_deleted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date_time");
          final int _cursorIndexOfEndDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date_time");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "is_all_day");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_id");
          final int _cursorIndexOfRecurrenceRule = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence_rule");
          final int _cursorIndexOfReminderMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_minutes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final LocalDateTime _tmpStartDateTime;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDateTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStartDateTime);
            }
            final LocalDateTime _tmp_1 = __dateTimeConverter.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartDateTime = _tmp_1;
            }
            final LocalDateTime _tmpEndDateTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndDateTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEndDateTime);
            }
            final LocalDateTime _tmp_3 = __dateTimeConverter.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndDateTime = _tmp_3;
            }
            final LocalDateTime _tmpStartTime;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalDateTime _tmp_5 = __dateTimeConverter.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_5;
            }
            final LocalDateTime _tmpEndTime;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalDateTime _tmp_7 = __dateTimeConverter.toLocalDateTime(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_7;
            }
            final boolean _tmpIsAllDay;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsAllDay);
            _tmpIsAllDay = _tmp_8 != 0;
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRecurrenceRule;
            if (_cursor.isNull(_cursorIndexOfRecurrenceRule)) {
              _tmpRecurrenceRule = null;
            } else {
              _tmpRecurrenceRule = _cursor.getString(_cursorIndexOfRecurrenceRule);
            }
            final Integer _tmpReminderMinutes;
            if (_cursor.isNull(_cursorIndexOfReminderMinutes)) {
              _tmpReminderMinutes = null;
            } else {
              _tmpReminderMinutes = _cursor.getInt(_cursorIndexOfReminderMinutes);
            }
            final boolean _tmpIsSynced;
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_9 != 0;
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_10;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_10 = null;
            } else {
              _tmp_10 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_11 = __dateTimeConverter.toLocalDateTime(_tmp_10);
            if (_tmp_11 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_11;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_12;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_12 = null;
            } else {
              _tmp_12 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_13 = __dateTimeConverter.toLocalDateTime(_tmp_12);
            if (_tmp_13 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_13;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_14;
            _tmp_14 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_14 != 0;
            _item = new EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpStartTime,_tmpEndTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getEventBySyncId(final String syncId,
      final Continuation<? super EventEntity> $completion) {
    final String _sql = "SELECT * FROM events WHERE sync_id = ? AND is_deleted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, syncId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EventEntity>() {
      @Override
      @Nullable
      public EventEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date_time");
          final int _cursorIndexOfEndDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date_time");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "is_all_day");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_id");
          final int _cursorIndexOfRecurrenceRule = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence_rule");
          final int _cursorIndexOfReminderMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_minutes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final EventEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final LocalDateTime _tmpStartDateTime;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDateTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStartDateTime);
            }
            final LocalDateTime _tmp_1 = __dateTimeConverter.toLocalDateTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartDateTime = _tmp_1;
            }
            final LocalDateTime _tmpEndDateTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndDateTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEndDateTime);
            }
            final LocalDateTime _tmp_3 = __dateTimeConverter.toLocalDateTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndDateTime = _tmp_3;
            }
            final LocalDateTime _tmpStartTime;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalDateTime _tmp_5 = __dateTimeConverter.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_5;
            }
            final LocalDateTime _tmpEndTime;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalDateTime _tmp_7 = __dateTimeConverter.toLocalDateTime(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_7;
            }
            final boolean _tmpIsAllDay;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsAllDay);
            _tmpIsAllDay = _tmp_8 != 0;
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRecurrenceRule;
            if (_cursor.isNull(_cursorIndexOfRecurrenceRule)) {
              _tmpRecurrenceRule = null;
            } else {
              _tmpRecurrenceRule = _cursor.getString(_cursorIndexOfRecurrenceRule);
            }
            final Integer _tmpReminderMinutes;
            if (_cursor.isNull(_cursorIndexOfReminderMinutes)) {
              _tmpReminderMinutes = null;
            } else {
              _tmpReminderMinutes = _cursor.getInt(_cursorIndexOfReminderMinutes);
            }
            final boolean _tmpIsSynced;
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_9 != 0;
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_10;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_10 = null;
            } else {
              _tmp_10 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_11 = __dateTimeConverter.toLocalDateTime(_tmp_10);
            if (_tmp_11 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_11;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_12;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_12 = null;
            } else {
              _tmp_12 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_13 = __dateTimeConverter.toLocalDateTime(_tmp_12);
            if (_tmp_13 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_13;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_14;
            _tmp_14 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_14 != 0;
            _result = new EventEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartDateTime,_tmpEndDateTime,_tmpStartTime,_tmpEndTime,_tmpIsAllDay,_tmpLocation,_tmpColor,_tmpCalendarId,_tmpRecurrenceRule,_tmpReminderMinutes,_tmpIsSynced,_tmpSyncId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
