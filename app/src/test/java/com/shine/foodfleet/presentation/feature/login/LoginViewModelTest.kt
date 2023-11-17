package com.shine.foodfleet.presentation.feature.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.shine.foodfleet.data.repository.UserRepository
import com.shine.foodfleet.utils.ResultWrapper
import com.tools.MainCoroutineRule
import com.tools.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LoginViewModelTest {

    @MockK
    lateinit var repo: UserRepository

    private lateinit var viewModel: LoginViewModel

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(
        UnconfinedTestDispatcher()
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = spyk(LoginViewModel(repo))
        val updateResult = flow {
            emit(ResultWrapper.Success(true))
        }
        coEvery { repo.doLogin(any(), any()) } returns updateResult
    }

    @Test
    fun `test do login`() {
        viewModel.doLogin("email", "password")
        coVerify { repo.doLogin(any(), any()) }
    }

    @Test
    fun `login result live data`() {
        runTest {
            viewModel.doLogin("email", "password")
            coVerify { repo.doLogin(any(), any()) }
            val result = viewModel.loginResult.getOrAwaitValue()
            TestCase.assertEquals(result.payload, true)
        }
    }
}
