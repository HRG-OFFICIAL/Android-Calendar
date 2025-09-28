package com.moderncalendar.core.sync.di;

import com.moderncalendar.core.sync.CloudSyncRepository;
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
public final class SyncModule_ProvideCloudSyncRepositoryFactory implements Factory<CloudSyncRepository> {
  @Override
  public CloudSyncRepository get() {
    return provideCloudSyncRepository();
  }

  public static SyncModule_ProvideCloudSyncRepositoryFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CloudSyncRepository provideCloudSyncRepository() {
    return Preconditions.checkNotNullFromProvides(SyncModule.INSTANCE.provideCloudSyncRepository());
  }

  private static final class InstanceHolder {
    static final SyncModule_ProvideCloudSyncRepositoryFactory INSTANCE = new SyncModule_ProvideCloudSyncRepositoryFactory();
  }
}
