package com.example.nayanepal.feature.gov.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nayanepal.feature.gov.domain.model.Employee
import com.example.nayanepal.feature.gov.domain.repository.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val repository: EmployeeRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedEmployee = MutableStateFlow<Employee?>(null)
    val selectedEmployee: StateFlow<Employee?> = _selectedEmployee.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val employees: StateFlow<List<Employee>> = _searchQuery
        .flatMapLatest { query ->
            repository.searchEmployees(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectEmployee(employee: Employee) {
        _selectedEmployee.value = employee
    }

    fun clearSelectedEmployee() {
        _selectedEmployee.value = null
    }

    fun refreshData() {
        viewModelScope.launch {
            repository.refreshEmployees()
        }
    }

    fun showAddEmployeeDialog() {
        // Implementation for showing add dialog
    }

    fun navigateToSalarySection(employee: Employee) {
        // Navigation logic
    }
}
