package com.example.nayanepal.feature.profile.di

import com.example.nayanepal.feature.profile.data.repository.SSFRepositoryImpl
import com.example.nayanepal.feature.profile.domain.repository.SSFRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {

    @Binds
    @Singleton
    abstract fun bindSSFRepository(
        ssfRepositoryImpl: SSFRepositoryImpl
    ): SSFRepository
}
