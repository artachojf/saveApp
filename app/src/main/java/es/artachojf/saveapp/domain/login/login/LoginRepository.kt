package es.artachojf.saveapp.domain.login.login

import androidx.credentials.GetCredentialResponse
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.LoginError

interface LoginRepository {
    suspend fun loginWithGoogle(
        result: GetCredentialResponse,
        rawNonce: String
    ): Result<Unit, LoginError>

    suspend fun loginAnonymously(): Result<Unit, LoginError>
}