package com.moderncalendar.core.data.repository;

import com.moderncalendar.core.data.dao.EventDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
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
public final class EventRepository_Factory implements Factory<EventRepository> {
  private final Provider<EventDao> eventDaoProvider;

  public EventRepository_Factory(Provider<EventDao> eventDaoProvider) {
    this.eventDaoProvider = eventDaoProvider;
  }

  @Override
  public EventRepository get() {
    return newInstance(eventDaoProvider.get());
  }

  public static EventRepository_Factory create(javax.inject.Provider<EventDao> eventDaoProvider) {
    return new EventRepository_Factory(Providers.asDaggerProvider(eventDaoProvider));
  }

  public static EventRepository_Factory create(Provider<EventDao> eventDaoProvider) {
    return new EventRepository_Factory(eventDaoProvider);
  }

  public static EventRepository newInstance(EventDao eventDao) {
    return new EventRepository(eventDao);
  }
}
