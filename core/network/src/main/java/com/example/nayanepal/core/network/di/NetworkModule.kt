package com.example.nayanepal.core.network.di

import com.example.nayanepal.core.network.client.RetrofitBuilder
import com.example.nayanepal.core.network.domain.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(tokenProvider: TokenProvider): Retrofit {
        return RetrofitBuilder.buildRetrofit(tokenProvider, "https://api.nayanepal.com/v1/")
    }
}
