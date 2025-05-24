package es.artachojf.saveapp.domain.login.user

import com.google.common.truth.Truth.assertThat
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.LoginError
import es.artachojf.saveapp.domain.login.user.model.UserBusiness
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetLoggedUserTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var repository: LoggedUserRepository

    @InjectMockKs
    lateinit var useCase: GetLoggedUser

    @Before
    fun setUp() {
        clearMocks(repository)
    }

    @Test
    fun `given get logged user when user is logged then returns success`() = runTest {
        val expectedResult = Result.Success(UserBusiness("id-test", "email-test"))
        coEvery { repository.getLoggedUser() } returns expectedResult

        val result = useCase()

        assertThat(result).isEqualTo(expectedResult)
        coVerify { repository.getLoggedUser() }
    }

    @Test
    fun `given get logged user when user is not logged then returns null`() = runTest {
        val expectedResult = Result.Success(null)
        coEvery { repository.getLoggedUser() } returns expectedResult

        val result = useCase()

        assertThat(result).isEqualTo(expectedResult)
        coVerify { repository.getLoggedUser() }
    }

    @Test
    fun `given get logged user when repository exception then returns exception`() = runTest {
        val expectedResult = Result.Failure(LoginError.GetLoggedUserError)
        coEvery { repository.getLoggedUser() } returns expectedResult

        val result = useCase()

        assertThat(result).isEqualTo(expectedResult)
        coVerify { repository.getLoggedUser() }
    }
}