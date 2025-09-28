package com.moderncalendar.core.accessibility;

import android.content.Context;
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
public final class AccessibilityManager_Factory implements Factory<AccessibilityManager> {
  private final Provider<Context> contextProvider;

  public AccessibilityManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AccessibilityManager get() {
    return newInstance(contextProvider.get());
  }

  public static AccessibilityManager_Factory create(
      javax.inject.Provider<Context> contextProvider) {
    return new AccessibilityManager_Factory(Providers.asDaggerProvider(contextProvider));
  }

  public static AccessibilityManager_Factory create(Provider<Context> contextProvider) {
    return new AccessibilityManager_Factory(contextProvider);
  }

  public static AccessibilityManager newInstance(Context context) {
    return new AccessibilityManager(context);
  }
}
