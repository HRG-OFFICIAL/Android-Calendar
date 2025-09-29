package com.moderncalendar.core.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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
public final class FirebaseAuthRepository_Factory implements Factory<FirebaseAuthRepository> {
  private final Provider<FirebaseAuth> firebaseAuthProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  public FirebaseAuthRepository_Factory(Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    this.firebaseAuthProvider = firebaseAuthProvider;
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public FirebaseAuthRepository get() {
    return newInstance(firebaseAuthProvider.get(), firestoreProvider.get());
  }

  public static FirebaseAuthRepository_Factory create(Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    return new FirebaseAuthRepository_Factory(firebaseAuthProvider, firestoreProvider);
  }

  public static FirebaseAuthRepository newInstance(FirebaseAuth firebaseAuth,
      FirebaseFirestore firestore) {
    return new FirebaseAuthRepository(firebaseAuth, firestore);
  }
}
