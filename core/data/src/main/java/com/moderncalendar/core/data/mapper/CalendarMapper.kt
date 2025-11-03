package com.moderncalendar.core.data.mapper

import com.moderncalendar.core.common.model.Calendar
import com.moderncalendar.core.common.model.CalendarType
import com.moderncalendar.core.data.entity.CalendarEntity

/**
 * Mapper functions to convert between Calendar domain model and CalendarEntity data model
 */

fun CalendarEntity.toDomainModel(): Calendar {
    return Calendar(
        id = id,
        name = name,
        description = description,
        color = color,
        isVisible = isVisible,
        isPrimary = isPrimary,
        calendarType = com.moderncalendar.core.common.model.CalendarType.valueOf(calendarType.name),
        accountEmail = accountEmail,
        syncId = syncId,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isDeleted = isDeleted
    )
}

fun Calendar.toDataEntity(): CalendarEntity {
    return CalendarEntity(
        id = id,
        name = name,
        description = description,
        color = color,
        isVisible = isVisible,
        isPrimary = isPrimary,
        calendarType = com.moderncalendar.core.data.entity.CalendarType.valueOf(calendarType.name),
        accountEmail = accountEmail,
        syncId = syncId,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isDeleted = isDeleted
    )
}