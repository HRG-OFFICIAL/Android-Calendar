package com.moderncalendar.core.data.mapper

import com.moderncalendar.core.common.model.Event
import com.moderncalendar.core.common.model.EventPriority
import com.moderncalendar.core.data.entity.EventEntity

/**
 * Mapper functions to convert between Event domain model and EventEntity data model
 */

fun EventEntity.toDomainModel(): Event {
    return Event(
        id = id,
        title = title,
        description = description,
        location = location,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        isAllDay = isAllDay,
        color = color,
        recurrenceRule = recurrenceRule,
        reminderMinutes = reminderMinutes,
        isCompleted = isCompleted,
        priority = com.moderncalendar.core.common.model.EventPriority.valueOf(priority.name),
        category = category,
        attendees = attendees,
        attachments = attachments,
        createdAt = createdAt,
        updatedAt = updatedAt,
        userId = userId,
        isSynced = isSynced,
        serverId = serverId
    )
}

fun Event.toDataEntity(): EventEntity {
    return EventEntity(
        id = id,
        title = title,
        description = description,
        location = location,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        isAllDay = isAllDay,
        color = color,
        recurrenceRule = recurrenceRule,
        reminderMinutes = reminderMinutes,
        isCompleted = isCompleted,
        priority = com.moderncalendar.core.data.entity.EventPriority.valueOf(priority.name),
        category = category,
        attendees = attendees,
        attachments = attachments,
        createdAt = createdAt,
        updatedAt = updatedAt,
        userId = userId,
        isSynced = isSynced,
        serverId = serverId
    )
}