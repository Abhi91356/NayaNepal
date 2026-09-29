package com.example.nayanepal.feature.gov.domain.repository

import com.example.nayanepal.feature.gov.domain.model.Employee
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {
    fun getEmployees(): Flow<List<Employee>>
    suspend fun refreshEmployees()
    fun searchEmployees(query: String): Flow<List<Employee>>
}
