package com.moderncalendar.core.reminders.di;

import android.app.AlarmManager;
import android.content.Context;
import com.moderncalendar.core.reminders.ReminderManager;
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
public final class RemindersModule_ProvideReminderManagerFactory implements Factory<ReminderManager> {
  private final Provider<Context> contextProvider;

  private final Provider<AlarmManager> alarmManagerProvider;

  public RemindersModule_ProvideReminderManagerFactory(Provider<Context> contextProvider,
      Provider<AlarmManager> alarmManagerProvider) {
    this.contextProvider = contextProvider;
    this.alarmManagerProvider = alarmManagerProvider;
  }

  @Override
  public ReminderManager get() {
    return provideReminderManager(contextProvider.get(), alarmManagerProvider.get());
  }

  public static RemindersModule_ProvideReminderManagerFactory create(
      Provider<Context> contextProvider, Provider<AlarmManager> alarmManagerProvider) {
    return new RemindersModule_ProvideReminderManagerFactory(contextProvider, alarmManagerProvider);
  }

  public static ReminderManager provideReminderManager(Context context, AlarmManager alarmManager) {
    return Preconditions.checkNotNullFromProvides(RemindersModule.INSTANCE.provideReminderManager(context, alarmManager));
  }
}
