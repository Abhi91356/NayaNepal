package com.example.nayanepal.core.auth.di

import android.content.Context
import com.example.nayanepal.core.auth.data.TokenProviderImpl
import com.example.nayanepal.core.auth.data.repository.AuthRepositoryImpl
import com.example.nayanepal.core.auth.domain.SessionManager
import com.example.nayanepal.core.auth.domain.repository.AuthRepository
import com.example.nayanepal.core.network.domain.TokenProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindTokenProvider(
        tokenProviderImpl: TokenProviderImpl
    ): TokenProvider

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
            return SessionManager(context)
        }

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        @Provides
        @Singleton
        fun provideFirebaseFirestore(): FirebaseFirestore {
            return FirebaseFirestore.getInstance()
        }

        @Provides
        @Singleton
        fun provideFirebaseFunctions(): FirebaseFunctions {
            return FirebaseFunctions.getInstance()
        }

        @Provides
        @Singleton
        fun provideFirebaseStorage(): FirebaseStorage {
            return FirebaseStorage.getInstance()
        }

        @Provides
        @Singleton
        fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
            return FirebaseAnalytics.getInstance(context)
        }
    }
}
