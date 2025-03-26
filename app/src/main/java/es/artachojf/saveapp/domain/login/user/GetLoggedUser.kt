package es.artachojf.saveapp.domain.login.user

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.user.model.UserBusiness
import javax.inject.Inject

class GetLoggedUser @Inject constructor(
    private val repository: LoggedUserRepository
) {
    suspend operator fun invoke(): Result<UserBusiness?, Unit> {
        return repository.getLoggedUser()
    }
}