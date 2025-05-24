package es.artachojf.saveapp.data.login.user

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.core.map
import es.artachojf.saveapp.data.login.LoginMemoryDataSource
import es.artachojf.saveapp.data.login.LoginSupabaseDataSource
import es.artachojf.saveapp.domain.login.LoginError
import es.artachojf.saveapp.domain.login.user.LoggedUserRepository
import es.artachojf.saveapp.domain.login.user.model.UserBusiness
import javax.inject.Inject

class LoggedUserRepositoryImpl @Inject constructor(
    private val dataSource: LoginSupabaseDataSource,
    private val memoryDataSource: LoginMemoryDataSource
): LoggedUserRepository {
    override suspend fun getLoggedUser(): Result<UserBusiness?, LoginError> {
        val memoryUser = memoryDataSource.getLoggedUser()
        memoryUser?.let {
            return Result.Success(it.toDomain())
        } ?: run {
            val result = dataSource.getLoggedUser()
            return result.map(
                mapSuccess = {
                    memoryDataSource.setLoggedUser(it)
                    it?.toDomain()
                },
                mapFailure = { it }
            )
        }
    }
}