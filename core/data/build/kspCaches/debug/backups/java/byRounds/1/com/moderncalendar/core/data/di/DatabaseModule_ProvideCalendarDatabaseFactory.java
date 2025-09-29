package com.moderncalendar.core.data.di;

import android.content.Context;
import com.moderncalendar.core.data.database.CalendarDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideCalendarDatabaseFactory implements Factory<CalendarDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideCalendarDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public CalendarDatabase get() {
    return provideCalendarDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideCalendarDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideCalendarDatabaseFactory(contextProvider);
  }

  public static CalendarDatabase provideCalendarDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCalendarDatabase(context));
  }
}
