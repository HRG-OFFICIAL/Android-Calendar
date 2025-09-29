package com.moderncalendar.core.analytics;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
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
public final class AnalyticsManager_Factory implements Factory<AnalyticsManager> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  private final Provider<FirebaseCrashlytics> crashlyticsProvider;

  public AnalyticsManager_Factory(Provider<FirebaseAnalytics> firebaseAnalyticsProvider,
      Provider<FirebaseCrashlytics> crashlyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
    this.crashlyticsProvider = crashlyticsProvider;
  }

  @Override
  public AnalyticsManager get() {
    return newInstance(firebaseAnalyticsProvider.get(), crashlyticsProvider.get());
  }

  public static AnalyticsManager_Factory create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider,
      Provider<FirebaseCrashlytics> crashlyticsProvider) {
    return new AnalyticsManager_Factory(firebaseAnalyticsProvider, crashlyticsProvider);
  }

  public static AnalyticsManager newInstance(FirebaseAnalytics firebaseAnalytics,
      FirebaseCrashlytics crashlytics) {
    return new AnalyticsManager(firebaseAnalytics, crashlytics);
  }
}
