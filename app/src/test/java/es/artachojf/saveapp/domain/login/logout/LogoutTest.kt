package es.artachojf.saveapp.domain.login.logout

import com.google.common.truth.Truth.assertThat
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.LoginError
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

class LogoutTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var repository: LogoutRepository

    @InjectMockKs
    lateinit var useCase: Logout

    @Before
    fun setUp() {
        clearMocks(repository)
    }

    @Test
    fun `given logout when repository succeeds then returns success`() = runTest {
        val expectedResult = Result.Success(Unit)
        coEvery { repository.logout() } returns expectedResult

        val result = useCase()

        assertThat(result).isEqualTo(expectedResult)
        coVerify { repository.logout() }
    }

    @Test
    fun `given logout when repository fails then returns failure`() = runTest {
        val expectedResult = Result.Failure(Unit)
        coEvery { repository.logout() } returns expectedResult

        val result = useCase()

        assertThat(result).isEqualTo(expectedResult)
        coVerify { repository.logout() }
    }
}