package es.artachojf.saveapp.domain.login.logout

import es.artachojf.saveapp.core.Result

interface LogoutRepository {
    suspend fun logout(): Result<Unit, Unit>
}