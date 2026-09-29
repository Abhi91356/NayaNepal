package com.example.nayanepal.feature.wallet.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nayanepal.feature.wallet.domain.model.Wallet
import com.example.nayanepal.feature.wallet.domain.usecase.GetWalletUseCase
import com.example.nayanepal.feature.wallet.presentation.state.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
/**
 * Enterprise Unit Test for WalletViewModel.
 * Ensures the robustness of the financial dashboard data flow.
 * Follows the strategy: ">80% code coverage for UseCases and ViewModels."
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WalletViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: WalletViewModel
    private val getWalletUseCase = mockk<GetWalletUseCase>()
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WalletViewModel(getWalletUseCase)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    @Test
    fun `loadWallet emits Success when use case returns data`() {
        // Arrange
        val mockWallet = Wallet("user_123", 100.0, "NPR")
        coEvery { getWalletUseCase() } returns mockWallet
        // Act
        viewModel.loadWallet()
        // Assert
        val state = viewModel.walletState.value
        assert(state is UiState.Success)
        assertEquals(mockWallet, (state as UiState.Success).data)
    fun `loadWallet emits Error when use case throws exception`() {
        val errorMessage = "Network Error"
        coEvery { getWalletUseCase() } throws Exception(errorMessage)
        assert(state is UiState.Error)
        assertEquals(errorMessage, (state as UiState.Error).message)
}
