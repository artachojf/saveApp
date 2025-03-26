package es.artachojf.saveapp.domain.login.anonymous

import es.artachojf.saveapp.core.Result

interface AnonymousLoginRepository {
    suspend fun loginAnonymously(): Result<Unit, Unit>
}