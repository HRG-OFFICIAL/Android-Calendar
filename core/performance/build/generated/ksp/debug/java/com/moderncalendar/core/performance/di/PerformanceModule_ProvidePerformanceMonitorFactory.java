package com.moderncalendar.core.performance.di;

import com.moderncalendar.core.performance.PerformanceMonitor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class PerformanceModule_ProvidePerformanceMonitorFactory implements Factory<PerformanceMonitor> {
  @Override
  public PerformanceMonitor get() {
    return providePerformanceMonitor();
  }

  public static PerformanceModule_ProvidePerformanceMonitorFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PerformanceMonitor providePerformanceMonitor() {
    return Preconditions.checkNotNullFromProvides(PerformanceModule.INSTANCE.providePerformanceMonitor());
  }

  private static final class InstanceHolder {
    static final PerformanceModule_ProvidePerformanceMonitorFactory INSTANCE = new PerformanceModule_ProvidePerformanceMonitorFactory();
  }
}
