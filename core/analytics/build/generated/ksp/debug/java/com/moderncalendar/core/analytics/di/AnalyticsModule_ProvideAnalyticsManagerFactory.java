package com.moderncalendar.core.analytics.di;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.moderncalendar.core.analytics.AnalyticsManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AnalyticsModule_ProvideAnalyticsManagerFactory implements Factory<AnalyticsManager> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  private final Provider<FirebaseCrashlytics> crashlyticsProvider;

  public AnalyticsModule_ProvideAnalyticsManagerFactory(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider,
      Provider<FirebaseCrashlytics> crashlyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
    this.crashlyticsProvider = crashlyticsProvider;
  }

  @Override
  public AnalyticsManager get() {
    return provideAnalyticsManager(firebaseAnalyticsProvider.get(), crashlyticsProvider.get());
  }

  public static AnalyticsModule_ProvideAnalyticsManagerFactory create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider,
      Provider<FirebaseCrashlytics> crashlyticsProvider) {
    return new AnalyticsModule_ProvideAnalyticsManagerFactory(firebaseAnalyticsProvider, crashlyticsProvider);
  }

  public static AnalyticsManager provideAnalyticsManager(FirebaseAnalytics firebaseAnalytics,
      FirebaseCrashlytics crashlytics) {
    return Preconditions.checkNotNullFromProvides(AnalyticsModule.INSTANCE.provideAnalyticsManager(firebaseAnalytics, crashlytics));
  }
}
