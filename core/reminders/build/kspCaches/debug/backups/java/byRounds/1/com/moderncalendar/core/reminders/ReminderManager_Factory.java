package com.moderncalendar.core.reminders;

import android.app.AlarmManager;
import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ReminderManager_Factory implements Factory<ReminderManager> {
  private final Provider<Context> contextProvider;

  private final Provider<AlarmManager> alarmManagerProvider;

  public ReminderManager_Factory(Provider<Context> contextProvider,
      Provider<AlarmManager> alarmManagerProvider) {
    this.contextProvider = contextProvider;
    this.alarmManagerProvider = alarmManagerProvider;
  }

  @Override
  public ReminderManager get() {
    return newInstance(contextProvider.get(), alarmManagerProvider.get());
  }

  public static ReminderManager_Factory create(Provider<Context> contextProvider,
      Provider<AlarmManager> alarmManagerProvider) {
    return new ReminderManager_Factory(contextProvider, alarmManagerProvider);
  }

  public static ReminderManager newInstance(Context context, AlarmManager alarmManager) {
    return new ReminderManager(context, alarmManager);
  }
}
