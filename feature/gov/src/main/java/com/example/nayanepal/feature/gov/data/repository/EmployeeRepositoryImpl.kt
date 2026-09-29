package com.example.nayanepal.feature.gov.data.repository

import com.example.nayanepal.feature.gov.domain.model.AbroadCompany
import com.example.nayanepal.feature.gov.domain.model.Employee
import com.example.nayanepal.feature.gov.domain.model.Office
import com.example.nayanepal.feature.gov.domain.repository.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeeRepositoryImpl @Inject constructor() : EmployeeRepository {
    private val _employees = MutableStateFlow<List<Employee>>(emptyList())

    init {
        // Initial mock data
        _employees.value = listOf(
            Employee(
                id = "1",
                userId = "u1",
                fullName = "Ram Bahadur",
                nepaliName = "राम बहादुर",
                phone = "9841234567",
                designation = "Senior Engineer",
                department = "Civil",
                employmentType = "domestic",
                office = Office(
                    id = "o1",
                    officeName = "Department of Roads",
                    district = "Kathmandu",
                    region = "Kathmandu Valley"
                )
            ),
            Employee(
                id = "2",
                userId = "u2",
                fullName = "Sita Devi",
                nepaliName = "सीता देवी",
                phone = "9800000001",
                designation = "Software Developer",
                employmentType = "abroad",
                abroadCompany = AbroadCompany(
                    id = "c1",
                    companyName = "Global Tech Corp",
                    country = "USA",
                    isVerified = true,
                    isTrusted = true,
                    trustScore = 95
                )
            )
        )
    }

    override fun getEmployees(): Flow<List<Employee>> = _employees

    override suspend fun refreshEmployees() {
        // In a real app, this would fetch from an API
    }

    override fun searchEmployees(query: String): Flow<List<Employee>> {
        return _employees.map { list ->
            if (query.isBlank()) list
            else list.filter { 
                it.fullName.contains(query, ignoreCase = true) || 
                it.office?.officeName?.contains(query, ignoreCase = true) == true ||
                it.abroadCompany?.companyName?.contains(query, ignoreCase = true) == true
            }
        }
    }
}
