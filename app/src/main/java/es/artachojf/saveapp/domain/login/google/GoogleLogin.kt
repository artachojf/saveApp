package es.artachojf.saveapp.domain.login.google

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.google.GoogleLoginRepository
import es.artachojf.saveapp.domain.login.google.model.GoogleLoginInput
import javax.inject.Inject

class GoogleLogin @Inject constructor(
    private val repository: GoogleLoginRepository
) {
    suspend operator fun invoke(input: GoogleLoginInput): Result<Unit, Unit> {
        return repository.loginWithGoogle(input)
    }
}