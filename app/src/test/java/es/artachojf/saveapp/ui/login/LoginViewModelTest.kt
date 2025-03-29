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
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun `initial uiState is Idle`() = runTest {
        assertThat(viewModel.uiState.value)
            .isEqualTo(LoginUIState.Idle())
    }

    @Test
    fun `given user already logged when app initiates then state success`() = runTest {
        coEvery { getLoggedUserUseCase() } returns Result.Success(UserBusiness())

        advanceUntilIdle()
        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Idle::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Success::class.java)
        }
    }

    @Test
    fun `given user not logged when app initiates then state idle`() = runTest {
        coEvery { getLoggedUserUseCase() } returns Result.Success(null)

        advanceUntilIdle()
        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Idle::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Idle::class.java)
        }
    }

    @Test
    fun `given error fetching user when app initiates then state idle`() = runTest {
        coEvery { getLoggedUserUseCase() } returns Result.Failure(LoginError.GetLoggedUserError)

        advanceUntilIdle()
        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Idle::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Loading::class.java)

            val idleState = awaitItem()
            assertThat(idleState).isInstanceOf(LoginUIState.Idle::class.java)
            assertThat((idleState as LoginUIState.Idle).error).isNotNull()
        }
    }

    @Test
    fun `when successful login with google then state success`() = runTest(testDispatcher) {
        coEvery { getLoggedUserUseCase() } returns Result.Success(null)

        val credentialResponse = mockk<GetCredentialResponse>()
        coEvery {
            loginUseCase(
                LoginMethod.GoogleLogin(credentialResponse, "nonce-test")
            )
        } returns Result.Success(Unit)

        advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Idle::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Idle::class.java)

            viewModel.login(LoginMethod.GoogleLogin(credentialResponse, "nonce-test"))
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Success::class.java)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when error login with google then state idle`() = runTest(testDispatcher) {
        coEvery { getLoggedUserUseCase() } returns Result.Success(null)

        val credentialResponse = mockk<GetCredentialResponse>()
        coEvery {
            loginUseCase(
                LoginMethod.GoogleLogin(credentialResponse, "nonce-test")
            )
        } returns Result.Failure(LoginError.GenericLoginError)

        advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Idle::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Idle::class.java)

            viewModel.login(LoginMethod.GoogleLogin(credentialResponse, "nonce-test"))
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Loading::class.java)

            val idleState = awaitItem()
            assertThat(idleState).isInstanceOf(LoginUIState.Idle::class.java)
            assertThat((idleState as LoginUIState.Idle).error).isNotNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when successful anonymous login then state success`() = runTest(testDispatcher) {
        coEvery { getLoggedUserUseCase() } returns Result.Success(null)

        val credentialResponse = mockk<GetCredentialResponse>()
        coEvery {
            loginUseCase(
                LoginMethod.AnonymousLogin
            )
        } returns Result.Success(Unit)

        advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Idle::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Idle::class.java)

            viewModel.login(LoginMethod.AnonymousLogin)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Success::class.java)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when error anonymous login then state idle`() = runTest(testDispatcher) {
        coEvery { getLoggedUserUseCase() } returns Result.Success(null)

        val credentialResponse = mockk<GetCredentialResponse>()
        coEvery {
            loginUseCase(
                LoginMethod.AnonymousLogin
            )
        } returns Result.Failure(LoginError.GenericLoginError)

        advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Idle::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Idle::class.java)

            viewModel.login(LoginMethod.AnonymousLogin)
            assertThat(awaitItem()).isInstanceOf(LoginUIState.Loading::class.java)

            val idleState = awaitItem()
            assertThat(idleState).isInstanceOf(LoginUIState.Idle::class.java)
            assertThat((idleState as LoginUIState.Idle).error).isNotNull()

            cancelAndIgnoreRemainingEvents()
        }
    }
}