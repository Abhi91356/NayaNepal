package com.example.nayanepal.feature.profile.data.repository

import com.example.nayanepal.feature.profile.domain.model.AbroadCompanyDetails
import com.example.nayanepal.feature.profile.domain.model.EmploymentStatus
import com.example.nayanepal.feature.profile.domain.model.SSFRecord
import com.example.nayanepal.feature.profile.domain.repository.SSFRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SSFRepositoryImpl @Inject constructor() : SSFRepository {
    
    override fun getSSFContributions(): Flow<List<SSFRecord>> = flow {
        val records = listOf(
            SSFRecord("1", "Nepal Tech Corp", "Kathmandu", "July 2026", 5000.0, 1784300000000L, "Verified"),
            SSFRecord("2", "Nepal Tech Corp", "Kathmandu", "June 2026", 5000.0, 1781700000000L, "Verified"),
            SSFRecord("3", "Nepal Tech Corp", "Kathmandu", "May 2026", 5000.0, 1779100000000L, "Verified")
        )
        emit(records)
    }

    override fun getEmploymentStatus(): Flow<EmploymentStatus> = flow {
        val status = EmploymentStatus(
            workplace = "Nepal Tech Corp",
            officeLocation = "Kathmandu, Nepal",
            salary = 75000.0,
            position = "Senior Software Engineer",
            startDate = 1735689600000L,
            isVerified = true,
            isAbroad = false
        )
        emit(status)
    }
}
