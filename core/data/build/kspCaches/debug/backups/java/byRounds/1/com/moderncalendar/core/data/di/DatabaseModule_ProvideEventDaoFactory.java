package com.moderncalendar.core.data.di;

import com.moderncalendar.core.data.dao.EventDao;
import com.moderncalendar.core.data.database.CalendarDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
    "deprecation"
})
public final class DatabaseModule_ProvideEventDaoFactory implements Factory<EventDao> {
  private final Provider<CalendarDatabase> databaseProvider;

  public DatabaseModule_ProvideEventDaoFactory(Provider<CalendarDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public EventDao get() {
    return provideEventDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideEventDaoFactory create(
      Provider<CalendarDatabase> databaseProvider) {
    return new DatabaseModule_ProvideEventDaoFactory(databaseProvider);
  }

  public static EventDao provideEventDao(CalendarDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideEventDao(database));
  }
}
