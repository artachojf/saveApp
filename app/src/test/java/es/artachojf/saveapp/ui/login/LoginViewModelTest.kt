package es.artachojf.saveapp.ui.login

import androidx.credentials.GetCredentialResponse
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.LoginError
import es.artachojf.saveapp.domain.login.login.Login
import es.artachojf.saveapp.domain.login.login.model.LoginMethod
import es.artachojf.saveapp.domain.login.user.GetLoggedUser
import es.artachojf.saveapp.domain.login.user.model.UserBusiness
import es.artachojf.saveapp.ui.login.model.LoginUIEvent
import es.artachojf.saveapp.ui.login.model.LoginUIState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var loginUseCase: Login

    @MockK
    lateinit var getLoggedUserUseCase: GetLoggedUser

    lateinit var viewModel: LoginViewModel

    val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(
            testDispatcher,
            loginUseCase,
            getLoggedUserUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState initial value is default`() = runTest {
        assertThat(viewModel.uiState.value)
            .isEqualTo(LoginUIState())
    }

    @Test
    fun `given user already logged when app initiates then success event`() = runTest {
        coEvery { getLoggedUserUseCase() } returns Result.Success(UserBusiness())

        advanceUntilIdle()
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(LoginUIState(isLoading = false))
            assertThat(awaitItem()).isEqualTo(LoginUIState(isLoading = true))
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiEvent.test {
            assertThat(awaitItem()).isEqualTo(LoginUIEvent.LoginSuccess)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given user not logged when app initiates then no event`() = runTest {
        coEvery { getLoggedUserUseCase() } returns Result.Success(null)

        advanceUntilIdle()
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(LoginUIState(isLoading = false))
            assertThat(awaitItem()).isEqualTo(LoginUIState(isLoading = true))
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiEvent.test {
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given error fetching user when app initiates then error event`() = runTest {
        coEvery { getLoggedUserUseCase() } returns Result.Failure(LoginError.GetLoggedUserError)

        advanceUntilIdle()
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(LoginUIState(isLoading = false))
            assertThat(awaitItem()).isEqualTo(LoginUIState(isLoading = true))
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiEvent.test {
            assertThat(awaitItem()).isInstanceOf(LoginUIEvent.Error::class.java)
        }
    }

    @Test
    fun `when successful login with google then success`() = runTest(testDispatcher) {
        coEvery { getLoggedUserUseCase() } returns Result.Success(null)

        val credentialResponse = mockk<GetCredentialResponse>()
        coEvery {
            loginUseCase(
                LoginMethod.GoogleLogin(credentialResponse, "nonce-test")
            )
        } returns Result.Success(Unit)

        advanceUntilIdle()
        viewModel.login(LoginMethod.GoogleLogin(credentialResponse, "nonce-test"))

        viewModel.uiEvent.test {
            assertThat(awaitItem()).isEqualTo(LoginUIEvent.LoginSuccess)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when error login with google then error event`() = runTest(testDispatcher) {
        coEvery { getLoggedUserUseCase() } returns Result.Success(null)

        val credentialResponse = mockk<GetCredentialResponse>()
        coEvery {
            loginUseCase(
                LoginMethod.GoogleLogin(credentialResponse, "nonce-test")
            )
        } returns Result.Failure(LoginError.GenericLoginError)

        advanceUntilIdle()
        viewModel.login(LoginMethod.GoogleLogin(credentialResponse, "nonce-test"))

        viewModel.uiEvent.test {
            assertThat(awaitItem()).isEqualTo(LoginUIEvent.Error(LoginError.GenericLoginError))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when successful anonymous login then success`() = runTest(testDispatcher) {
        coEvery { getLoggedUserUseCase() } returns Result.Success(null)

        coEvery {
            loginUseCase(
                LoginMethod.AnonymousLogin
            )
        } returns Result.Success(Unit)

        advanceUntilIdle()
        viewModel.login(LoginMethod.AnonymousLogin)

        viewModel.uiEvent.test {
            assertThat(awaitItem()).isEqualTo(LoginUIEvent.LoginSuccess)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when error anonymous login then state idle`() = runTest(testDispatcher) {
        coEvery { getLoggedUserUseCase() } returns Result.Success(null)

        coEvery {
            loginUseCase(
                LoginMethod.AnonymousLogin
            )
        } returns Result.Failure(LoginError.GenericLoginError)

        advanceUntilIdle()
        viewModel.login(LoginMethod.AnonymousLogin)

        viewModel.uiEvent.test {
            assertThat(awaitItem()).isEqualTo(LoginUIEvent.Error(LoginError.GenericLoginError))
            cancelAndIgnoreRemainingEvents()
        }
    }
}