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
import com.moderncalendar.core.data.entity.CalendarEntity;
import com.moderncalendar.core.data.entity.CalendarType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
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
public final class CalendarDao_Impl implements CalendarDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CalendarEntity> __insertionAdapterOfCalendarEntity;

  private final DateTimeConverter __dateTimeConverter = new DateTimeConverter();

  private final EntityDeletionOrUpdateAdapter<CalendarEntity> __updateAdapterOfCalendarEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCalendar;

  private final SharedSQLiteStatement __preparedStmtOfUnsetPrimaryCalendar;

  private final SharedSQLiteStatement __preparedStmtOfSetPrimaryCalendar;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCalendarVisibility;

  private final SharedSQLiteStatement __preparedStmtOfMarkCalendarAsSynced;

  private final SharedSQLiteStatement __preparedStmtOfMarkCalendarAsUnsynced;

  public CalendarDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCalendarEntity = new EntityInsertionAdapter<CalendarEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `calendars` (`id`,`name`,`description`,`color`,`is_visible`,`is_primary`,`calendar_type`,`account_email`,`sync_id`,`is_synced`,`created_at`,`updated_at`,`is_deleted`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CalendarEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindString(4, entity.getColor());
        final int _tmp = entity.isVisible() ? 1 : 0;
        statement.bindLong(5, _tmp);
        final int _tmp_1 = entity.isPrimary() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        statement.bindString(7, __CalendarType_enumToString(entity.getCalendarType()));
        if (entity.getAccountEmail() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getAccountEmail());
        }
        if (entity.getSyncId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSyncId());
        }
        final int _tmp_2 = entity.isSynced() ? 1 : 0;
        statement.bindLong(10, _tmp_2);
        final String _tmp_3 = __dateTimeConverter.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, _tmp_3);
        }
        final String _tmp_4 = __dateTimeConverter.fromLocalDateTime(entity.getUpdatedAt());
        if (_tmp_4 == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, _tmp_4);
        }
        final int _tmp_5 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(13, _tmp_5);
      }
    };
    this.__updateAdapterOfCalendarEntity = new EntityDeletionOrUpdateAdapter<CalendarEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `calendars` SET `id` = ?,`name` = ?,`description` = ?,`color` = ?,`is_visible` = ?,`is_primary` = ?,`calendar_type` = ?,`account_email` = ?,`sync_id` = ?,`is_synced` = ?,`created_at` = ?,`updated_at` = ?,`is_deleted` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CalendarEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindString(4, entity.getColor());
        final int _tmp = entity.isVisible() ? 1 : 0;
        statement.bindLong(5, _tmp);
        final int _tmp_1 = entity.isPrimary() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        statement.bindString(7, __CalendarType_enumToString(entity.getCalendarType()));
        if (entity.getAccountEmail() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getAccountEmail());
        }
        if (entity.getSyncId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSyncId());
        }
        final int _tmp_2 = entity.isSynced() ? 1 : 0;
        statement.bindLong(10, _tmp_2);
        final String _tmp_3 = __dateTimeConverter.fromLocalDateTime(entity.getCreatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, _tmp_3);
        }
        final String _tmp_4 = __dateTimeConverter.fromLocalDateTime(entity.getUpdatedAt());
        if (_tmp_4 == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, _tmp_4);
        }
        final int _tmp_5 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(13, _tmp_5);
        statement.bindString(14, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteCalendar = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE calendars SET is_deleted = 1, updated_at = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUnsetPrimaryCalendar = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE calendars SET is_primary = 0 WHERE id != ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetPrimaryCalendar = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE calendars SET is_primary = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateCalendarVisibility = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE calendars SET is_visible = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkCalendarAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE calendars SET is_synced = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkCalendarAsUnsynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE calendars SET is_synced = 0 WHERE sync_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertCalendar(final CalendarEntity calendar,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCalendarEntity.insert(calendar);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertCalendars(final List<CalendarEntity> calendars,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCalendarEntity.insert(calendars);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCalendar(final CalendarEntity calendar,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCalendarEntity.handle(calendar);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCalendar(final String calendarId, final LocalDateTime updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCalendar.acquire();
        int _argIndex = 1;
        final String _tmp = __dateTimeConverter.fromLocalDateTime(updatedAt);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp);
        }
        _argIndex = 2;
        _stmt.bindString(_argIndex, calendarId);
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
          __preparedStmtOfDeleteCalendar.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object unsetPrimaryCalendar(final String calendarId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUnsetPrimaryCalendar.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, calendarId);
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
          __preparedStmtOfUnsetPrimaryCalendar.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setPrimaryCalendar(final String calendarId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetPrimaryCalendar.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, calendarId);
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
          __preparedStmtOfSetPrimaryCalendar.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCalendarVisibility(final String calendarId, final boolean isVisible,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCalendarVisibility.acquire();
        int _argIndex = 1;
        final int _tmp = isVisible ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, calendarId);
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
          __preparedStmtOfUpdateCalendarVisibility.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markCalendarAsSynced(final String calendarId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkCalendarAsSynced.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, calendarId);
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
          __preparedStmtOfMarkCalendarAsSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markCalendarAsUnsynced(final String syncId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkCalendarAsUnsynced.acquire();
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
          __preparedStmtOfMarkCalendarAsUnsynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CalendarEntity>> getAllCalendars() {
    final String _sql = "SELECT * FROM calendars WHERE is_deleted = 0 ORDER BY is_primary DESC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"calendars"}, new Callable<List<CalendarEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "is_visible");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "is_primary");
          final int _cursorIndexOfCalendarType = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_type");
          final int _cursorIndexOfAccountEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "account_email");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final List<CalendarEntity> _result = new ArrayList<CalendarEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final boolean _tmpIsVisible;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVisible);
            _tmpIsVisible = _tmp != 0;
            final boolean _tmpIsPrimary;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp_1 != 0;
            final CalendarType _tmpCalendarType;
            _tmpCalendarType = __CalendarType_stringToEnum(_cursor.getString(_cursorIndexOfCalendarType));
            final String _tmpAccountEmail;
            if (_cursor.isNull(_cursorIndexOfAccountEmail)) {
              _tmpAccountEmail = null;
            } else {
              _tmpAccountEmail = _cursor.getString(_cursorIndexOfAccountEmail);
            }
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final boolean _tmpIsSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_2 != 0;
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_4 = __dateTimeConverter.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_4;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_6 = __dateTimeConverter.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_6;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_7 != 0;
            _item = new CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
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
  public Object getCalendarById(final String calendarId,
      final Continuation<? super CalendarEntity> $completion) {
    final String _sql = "SELECT * FROM calendars WHERE id = ? AND is_deleted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, calendarId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CalendarEntity>() {
      @Override
      @Nullable
      public CalendarEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "is_visible");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "is_primary");
          final int _cursorIndexOfCalendarType = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_type");
          final int _cursorIndexOfAccountEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "account_email");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final CalendarEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final boolean _tmpIsVisible;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVisible);
            _tmpIsVisible = _tmp != 0;
            final boolean _tmpIsPrimary;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp_1 != 0;
            final CalendarType _tmpCalendarType;
            _tmpCalendarType = __CalendarType_stringToEnum(_cursor.getString(_cursorIndexOfCalendarType));
            final String _tmpAccountEmail;
            if (_cursor.isNull(_cursorIndexOfAccountEmail)) {
              _tmpAccountEmail = null;
            } else {
              _tmpAccountEmail = _cursor.getString(_cursorIndexOfAccountEmail);
            }
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final boolean _tmpIsSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_2 != 0;
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_4 = __dateTimeConverter.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_4;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_6 = __dateTimeConverter.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_6;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_7 != 0;
            _result = new CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
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
  public Flow<List<CalendarEntity>> getVisibleCalendars() {
    final String _sql = "SELECT * FROM calendars WHERE is_visible = 1 AND is_deleted = 0 ORDER BY is_primary DESC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"calendars"}, new Callable<List<CalendarEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "is_visible");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "is_primary");
          final int _cursorIndexOfCalendarType = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_type");
          final int _cursorIndexOfAccountEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "account_email");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final List<CalendarEntity> _result = new ArrayList<CalendarEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final boolean _tmpIsVisible;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVisible);
            _tmpIsVisible = _tmp != 0;
            final boolean _tmpIsPrimary;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp_1 != 0;
            final CalendarType _tmpCalendarType;
            _tmpCalendarType = __CalendarType_stringToEnum(_cursor.getString(_cursorIndexOfCalendarType));
            final String _tmpAccountEmail;
            if (_cursor.isNull(_cursorIndexOfAccountEmail)) {
              _tmpAccountEmail = null;
            } else {
              _tmpAccountEmail = _cursor.getString(_cursorIndexOfAccountEmail);
            }
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final boolean _tmpIsSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_2 != 0;
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_4 = __dateTimeConverter.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_4;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_6 = __dateTimeConverter.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_6;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_7 != 0;
            _item = new CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
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
  public Flow<List<CalendarEntity>> getCalendarsByType(final String type) {
    final String _sql = "SELECT * FROM calendars WHERE calendar_type = ? AND is_deleted = 0 ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"calendars"}, new Callable<List<CalendarEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "is_visible");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "is_primary");
          final int _cursorIndexOfCalendarType = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_type");
          final int _cursorIndexOfAccountEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "account_email");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final List<CalendarEntity> _result = new ArrayList<CalendarEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final boolean _tmpIsVisible;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVisible);
            _tmpIsVisible = _tmp != 0;
            final boolean _tmpIsPrimary;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp_1 != 0;
            final CalendarType _tmpCalendarType;
            _tmpCalendarType = __CalendarType_stringToEnum(_cursor.getString(_cursorIndexOfCalendarType));
            final String _tmpAccountEmail;
            if (_cursor.isNull(_cursorIndexOfAccountEmail)) {
              _tmpAccountEmail = null;
            } else {
              _tmpAccountEmail = _cursor.getString(_cursorIndexOfAccountEmail);
            }
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final boolean _tmpIsSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_2 != 0;
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_4 = __dateTimeConverter.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_4;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_6 = __dateTimeConverter.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_6;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_7 != 0;
            _item = new CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
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
  public Object getPrimaryCalendar(final Continuation<? super CalendarEntity> $completion) {
    final String _sql = "SELECT * FROM calendars WHERE is_primary = 1 AND is_deleted = 0 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CalendarEntity>() {
      @Override
      @Nullable
      public CalendarEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "is_visible");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "is_primary");
          final int _cursorIndexOfCalendarType = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_type");
          final int _cursorIndexOfAccountEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "account_email");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final CalendarEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final boolean _tmpIsVisible;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVisible);
            _tmpIsVisible = _tmp != 0;
            final boolean _tmpIsPrimary;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp_1 != 0;
            final CalendarType _tmpCalendarType;
            _tmpCalendarType = __CalendarType_stringToEnum(_cursor.getString(_cursorIndexOfCalendarType));
            final String _tmpAccountEmail;
            if (_cursor.isNull(_cursorIndexOfAccountEmail)) {
              _tmpAccountEmail = null;
            } else {
              _tmpAccountEmail = _cursor.getString(_cursorIndexOfAccountEmail);
            }
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final boolean _tmpIsSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_2 != 0;
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_4 = __dateTimeConverter.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_4;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_6 = __dateTimeConverter.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_6;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_7 != 0;
            _result = new CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
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
  public Object getCalendarBySyncId(final String syncId,
      final Continuation<? super CalendarEntity> $completion) {
    final String _sql = "SELECT * FROM calendars WHERE sync_id = ? AND is_deleted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, syncId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CalendarEntity>() {
      @Override
      @Nullable
      public CalendarEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "is_visible");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "is_primary");
          final int _cursorIndexOfCalendarType = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_type");
          final int _cursorIndexOfAccountEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "account_email");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final CalendarEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final boolean _tmpIsVisible;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVisible);
            _tmpIsVisible = _tmp != 0;
            final boolean _tmpIsPrimary;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp_1 != 0;
            final CalendarType _tmpCalendarType;
            _tmpCalendarType = __CalendarType_stringToEnum(_cursor.getString(_cursorIndexOfCalendarType));
            final String _tmpAccountEmail;
            if (_cursor.isNull(_cursorIndexOfAccountEmail)) {
              _tmpAccountEmail = null;
            } else {
              _tmpAccountEmail = _cursor.getString(_cursorIndexOfAccountEmail);
            }
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final boolean _tmpIsSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_2 != 0;
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_4 = __dateTimeConverter.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_4;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_6 = __dateTimeConverter.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_6;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_7 != 0;
            _result = new CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
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
  public Object getUnsyncedCalendars(final Continuation<? super List<CalendarEntity>> $completion) {
    final String _sql = "SELECT * FROM calendars WHERE is_synced = 0 AND is_deleted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CalendarEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "is_visible");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "is_primary");
          final int _cursorIndexOfCalendarType = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_type");
          final int _cursorIndexOfAccountEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "account_email");
          final int _cursorIndexOfSyncId = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_id");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "is_synced");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_deleted");
          final List<CalendarEntity> _result = new ArrayList<CalendarEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final boolean _tmpIsVisible;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVisible);
            _tmpIsVisible = _tmp != 0;
            final boolean _tmpIsPrimary;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp_1 != 0;
            final CalendarType _tmpCalendarType;
            _tmpCalendarType = __CalendarType_stringToEnum(_cursor.getString(_cursorIndexOfCalendarType));
            final String _tmpAccountEmail;
            if (_cursor.isNull(_cursorIndexOfAccountEmail)) {
              _tmpAccountEmail = null;
            } else {
              _tmpAccountEmail = _cursor.getString(_cursorIndexOfAccountEmail);
            }
            final String _tmpSyncId;
            if (_cursor.isNull(_cursorIndexOfSyncId)) {
              _tmpSyncId = null;
            } else {
              _tmpSyncId = _cursor.getString(_cursorIndexOfSyncId);
            }
            final boolean _tmpIsSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_2 != 0;
            final LocalDateTime _tmpCreatedAt;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDateTime _tmp_4 = __dateTimeConverter.toLocalDateTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_4;
            }
            final LocalDateTime _tmpUpdatedAt;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final LocalDateTime _tmp_6 = __dateTimeConverter.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpUpdatedAt = _tmp_6;
            }
            final boolean _tmpIsDeleted;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_7 != 0;
            _item = new CalendarEntity(_tmpId,_tmpName,_tmpDescription,_tmpColor,_tmpIsVisible,_tmpIsPrimary,_tmpCalendarType,_tmpAccountEmail,_tmpSyncId,_tmpIsSynced,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __CalendarType_enumToString(@NonNull final CalendarType _value) {
    switch (_value) {
      case LOCAL: return "LOCAL";
      case GOOGLE: return "GOOGLE";
      case MICROSOFT: return "MICROSOFT";
      case CALDAV: return "CALDAV";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private CalendarType __CalendarType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "LOCAL": return CalendarType.LOCAL;
      case "GOOGLE": return CalendarType.GOOGLE;
      case "MICROSOFT": return CalendarType.MICROSOFT;
      case "CALDAV": return CalendarType.CALDAV;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
