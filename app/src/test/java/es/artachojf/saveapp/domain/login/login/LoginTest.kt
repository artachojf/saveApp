package es.artachojf.saveapp.domain.login.login

import androidx.credentials.GetCredentialResponse
import com.google.common.truth.Truth.assertThat
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.LoginError
import es.artachojf.saveapp.domain.login.login.model.LoginMethod
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var repository: LoginRepository

    @InjectMockKs
    lateinit var useCase: Login

    @Before
    fun setUp() {
        clearMocks(repository)
    }

    @Test
    fun `given anonymous login when repository succeeds then returns success`() = runTest {
        val expectedResult = Result.Success(Unit)
        coEvery { repository.loginAnonymously() } returns expectedResult

        val result = useCase(LoginMethod.AnonymousLogin)

        assertThat(result).isEqualTo(expectedResult)
        coVerify { repository.loginAnonymously() }
    }

    @Test
    fun `given anonymous login when repository fails then returns failure`() = runTest {
        val expectedResult = Result.Failure(LoginError.GenericLoginError)
        coEvery { repository.loginAnonymously() } returns expectedResult

        val result = useCase(LoginMethod.AnonymousLogin)

        assertThat(result).isEqualTo(expectedResult)
        coVerify { repository.loginAnonymously() }
    }

    @Test
    fun `given google login when repository succeeds then returns success`() = runTest {
        val expectedResult = Result.Success(Unit)
        coEvery { repository.loginWithGoogle(any(), any()) } returns expectedResult

        val credentialResponse = mockk<GetCredentialResponse>()
        val result = useCase(LoginMethod.GoogleLogin(credentialResponse, "nonce-test"))

        assertThat(result).isEqualTo(expectedResult)
        coVerify { repository.loginWithGoogle(any(), any()) }

    }

    @Test
    fun `given google login when repository fails then returns failure`() = runTest {
        val expectedResult = Result.Failure(LoginError.GenericLoginError)
        coEvery { repository.loginWithGoogle(any(), any()) } returns expectedResult

        val credentialResponse = mockk<GetCredentialResponse>()
        val result = useCase(LoginMethod.GoogleLogin(credentialResponse, "nonce-test"))

        assertThat(result).isEqualTo(expectedResult)
        coVerify { repository.loginWithGoogle(any(), any()) }
    }
}