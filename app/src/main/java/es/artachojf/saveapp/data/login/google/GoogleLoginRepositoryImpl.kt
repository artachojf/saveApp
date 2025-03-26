package es.artachojf.saveapp.data.login.google

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.data.login.LoginSupabaseDataSource
import es.artachojf.saveapp.domain.login.google.GoogleLoginRepository
import es.artachojf.saveapp.domain.login.google.model.GoogleLoginInput
import javax.inject.Inject

class GoogleLoginRepositoryImpl @Inject constructor(
    private val dataSource: LoginSupabaseDataSource
): GoogleLoginRepository {
    override suspend fun loginWithGoogle(input: GoogleLoginInput): Result<Unit, Unit> {
        return dataSource.loginWithGoogle(input.result, input.rawNonce)
    }
}