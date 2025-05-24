package es.artachojf.saveapp.domain.login.logout

import es.artachojf.saveapp.core.Result
import javax.inject.Inject

class Logout @Inject constructor(
    private val repository: LogoutRepository
) {
    suspend operator fun invoke(): Result<Unit, Unit> {
        return repository.logout()
    }
}