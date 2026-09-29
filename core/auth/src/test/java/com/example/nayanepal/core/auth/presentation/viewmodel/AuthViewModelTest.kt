package com.example.nayanepal.core.auth.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nayanepal.core.auth.domain.model.User
import com.example.nayanepal.core.auth.domain.repository.AuthRepository
import com.example.nayanepal.core.auth.presentation.state.LoginState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    private val testDispatcher = StandardTestDispatcher()
    
    private lateinit var viewModel: AuthViewModel
    private val authRepository = mockk<AuthRepository>()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun testLoginWithEmail_Success() = runTest {
        // Arrange
        val mockUser = User(
            id = "1",
            email = "test@example.com",
            name = "Test User"
        )
        
        coEvery { authRepository.loginWithEmail("test@example.com", "password") } returns Result.success(mockUser)
        
        // Act
        viewModel.loginWithEmail("test@example.com", "password")
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertEquals(LoginState.Success(mockUser), viewModel.loginState.value)
    }
    
    @Test
    fun testLoginWithEmail_Error() = runTest {
        // Arrange
        val errorMessage = "Invalid credentials"
        coEvery { authRepository.loginWithEmail("test@example.com", "password") } returns Result.failure(Exception(errorMessage))
        
        // Act
        viewModel.loginWithEmail("test@example.com", "password")
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertEquals(LoginState.Error(errorMessage), viewModel.loginState.value)
    }
}
