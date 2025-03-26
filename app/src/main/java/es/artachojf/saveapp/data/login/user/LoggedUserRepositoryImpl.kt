package es.artachojf.saveapp.data.login.user

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.core.map
import es.artachojf.saveapp.data.login.LoginSupabaseDataSource
import es.artachojf.saveapp.domain.login.user.LoggedUserRepository
import es.artachojf.saveapp.domain.login.user.model.UserBusiness
import javax.inject.Inject

class LoggedUserRepositoryImpl @Inject constructor(
    private val dataSource: LoginSupabaseDataSource
): LoggedUserRepository {
    override suspend fun getLoggedUser(): Result<UserBusiness?, Unit> {
        val result = dataSource.getLoggedUser()
        return result.map(
            mapSuccess = {
                it?.toDomain()
            },
            mapFailure = {}
        )
    }
}