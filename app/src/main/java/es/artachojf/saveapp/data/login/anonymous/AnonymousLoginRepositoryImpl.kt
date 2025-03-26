package es.artachojf.saveapp.data.login.anonymous

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.data.login.LoginSupabaseDataSource
import es.artachojf.saveapp.domain.login.anonymous.AnonymousLoginRepository
import javax.inject.Inject

class AnonymousLoginRepositoryImpl @Inject constructor(
    private val dataSource: LoginSupabaseDataSource
): AnonymousLoginRepository {
    override suspend fun loginAnonymously(): Result<Unit, Unit> {
        return dataSource.loginAnonymously()
    }
}