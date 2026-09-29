package com.example.nayanepal.feature.gov.di

import com.example.nayanepal.feature.gov.data.repository.EmployeeRepositoryImpl
import com.example.nayanepal.feature.gov.domain.repository.EmployeeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GovModule {

    @Binds
    @Singleton
    abstract fun bindEmployeeRepository(
        employeeRepositoryImpl: EmployeeRepositoryImpl
    ): EmployeeRepository
}
