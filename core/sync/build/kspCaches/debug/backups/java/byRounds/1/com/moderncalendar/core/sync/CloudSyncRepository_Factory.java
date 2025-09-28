package com.moderncalendar.core.sync;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
    "deprecation"
})
public final class CloudSyncRepository_Factory implements Factory<CloudSyncRepository> {
  @Override
  public CloudSyncRepository get() {
    return newInstance();
  }

  public static CloudSyncRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CloudSyncRepository newInstance() {
    return new CloudSyncRepository();
  }

  private static final class InstanceHolder {
    private static final CloudSyncRepository_Factory INSTANCE = new CloudSyncRepository_Factory();
  }
}
