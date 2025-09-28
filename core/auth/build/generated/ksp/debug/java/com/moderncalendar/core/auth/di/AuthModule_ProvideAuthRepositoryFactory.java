package com.moderncalendar.core.auth.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moderncalendar.core.auth.AuthRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AuthModule_ProvideAuthRepositoryFactory implements Factory<AuthRepository> {
  private final Provider<FirebaseAuth> firebaseAuthProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  public AuthModule_ProvideAuthRepositoryFactory(Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    this.firebaseAuthProvider = firebaseAuthProvider;
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public AuthRepository get() {
    return provideAuthRepository(firebaseAuthProvider.get(), firestoreProvider.get());
  }

  public static AuthModule_ProvideAuthRepositoryFactory create(
      javax.inject.Provider<FirebaseAuth> firebaseAuthProvider,
      javax.inject.Provider<FirebaseFirestore> firestoreProvider) {
    return new AuthModule_ProvideAuthRepositoryFactory(Providers.asDaggerProvider(firebaseAuthProvider), Providers.asDaggerProvider(firestoreProvider));
  }

  public static AuthModule_ProvideAuthRepositoryFactory create(
      Provider<FirebaseAuth> firebaseAuthProvider, Provider<FirebaseFirestore> firestoreProvider) {
    return new AuthModule_ProvideAuthRepositoryFactory(firebaseAuthProvider, firestoreProvider);
  }

  public static AuthRepository provideAuthRepository(FirebaseAuth firebaseAuth,
      FirebaseFirestore firestore) {
    return Preconditions.checkNotNullFromProvides(AuthModule.INSTANCE.provideAuthRepository(firebaseAuth, firestore));
  }
}
