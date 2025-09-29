package com.moderncalendar.core.data.repository;

import com.moderncalendar.core.data.dao.EventDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class RoomEventRepository_Factory implements Factory<RoomEventRepository> {
  private final Provider<EventDao> eventDaoProvider;

  public RoomEventRepository_Factory(Provider<EventDao> eventDaoProvider) {
    this.eventDaoProvider = eventDaoProvider;
  }

  @Override
  public RoomEventRepository get() {
    return newInstance(eventDaoProvider.get());
  }

  public static RoomEventRepository_Factory create(Provider<EventDao> eventDaoProvider) {
    return new RoomEventRepository_Factory(eventDaoProvider);
  }

  public static RoomEventRepository newInstance(EventDao eventDao) {
    return new RoomEventRepository(eventDao);
  }
}
