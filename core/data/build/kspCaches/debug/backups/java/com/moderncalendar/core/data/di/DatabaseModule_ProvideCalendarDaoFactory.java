package com.moderncalendar.core.data.di;

import com.moderncalendar.core.data.dao.CalendarDao;
import com.moderncalendar.core.data.database.CalendarDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class DatabaseModule_ProvideCalendarDaoFactory implements Factory<CalendarDao> {
  private final Provider<CalendarDatabase> databaseProvider;

  public DatabaseModule_ProvideCalendarDaoFactory(Provider<CalendarDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public CalendarDao get() {
    return provideCalendarDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideCalendarDaoFactory create(
      javax.inject.Provider<CalendarDatabase> databaseProvider) {
    return new DatabaseModule_ProvideCalendarDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideCalendarDaoFactory create(
      Provider<CalendarDatabase> databaseProvider) {
    return new DatabaseModule_ProvideCalendarDaoFactory(databaseProvider);
  }

  public static CalendarDao provideCalendarDao(CalendarDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCalendarDao(database));
  }
}
