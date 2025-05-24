package es.artachojf.saveapp.data.login.login

import androidx.credentials.GetCredentialResponse
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.data.login.LoginSupabaseDataSource
import es.artachojf.saveapp.domain.login.LoginError
import es.artachojf.saveapp.domain.login.login.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val dataSource: LoginSupabaseDataSource
): LoginRepository {
    override suspend fun loginWithGoogle(
        result: GetCredentialResponse,
        rawNonce: String
    ): Result<Unit, LoginError> {
        return dataSource.loginWithGoogle(result, rawNonce)
    }

    override suspend fun loginAnonymously(): Result<Unit, LoginError> {
        return dataSource.loginAnonymously()
    }
}