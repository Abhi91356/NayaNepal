package com.example.nayanepal.feature.profile.domain.repository

import com.example.nayanepal.feature.profile.domain.model.EmploymentStatus
import com.example.nayanepal.feature.profile.domain.model.SSFRecord
import kotlinx.coroutines.flow.Flow

interface SSFRepository {
    fun getSSFContributions(): Flow<List<SSFRecord>>
    fun getEmploymentStatus(): Flow<EmploymentStatus>
}
