package es.artachojf.saveapp.domain.login.user

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.user.model.UserBusiness

interface LoggedUserRepository {
    suspend fun getLoggedUser(): Result<UserBusiness?, Unit>
}