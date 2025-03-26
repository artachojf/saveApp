package es.artachojf.saveapp.data.login.logout

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.data.login.LoginSupabaseDataSource
import es.artachojf.saveapp.domain.login.logout.LogoutRepository
import javax.inject.Inject

class LogoutRepositoryImpl @Inject constructor(
    private val dataSource: LoginSupabaseDataSource
): LogoutRepository {
    override suspend fun logout(): Result<Unit, Unit> {
        return dataSource.logout()
    }
}