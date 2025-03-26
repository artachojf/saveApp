package es.artachojf.saveapp.domain.login.anonymous

import es.artachojf.saveapp.core.Result
import javax.inject.Inject

class AnonymousLogin @Inject constructor(
    private val repository: AnonymousLoginRepository
) {
    suspend operator fun invoke(): Result<Unit, Unit> {
        return repository.loginAnonymously()
    }
}