package com.moderncalendar.core.accessibility.di;

import android.content.Context;
import com.moderncalendar.core.accessibility.AccessibilityManager;
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
public final class AccessibilityModule_ProvideAccessibilityManagerFactory implements Factory<AccessibilityManager> {
  private final Provider<Context> contextProvider;

  public AccessibilityModule_ProvideAccessibilityManagerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AccessibilityManager get() {
    return provideAccessibilityManager(contextProvider.get());
  }

  public static AccessibilityModule_ProvideAccessibilityManagerFactory create(
      Provider<Context> contextProvider) {
    return new AccessibilityModule_ProvideAccessibilityManagerFactory(contextProvider);
  }

  public static AccessibilityManager provideAccessibilityManager(Context context) {
    return Preconditions.checkNotNullFromProvides(AccessibilityModule.INSTANCE.provideAccessibilityManager(context));
  }
}
