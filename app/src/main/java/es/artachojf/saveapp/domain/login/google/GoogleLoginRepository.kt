package es.artachojf.saveapp.domain.login.google

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.google.model.GoogleLoginInput

interface GoogleLoginRepository {
    suspend fun loginWithGoogle(input: GoogleLoginInput): Result<Unit, Unit>
}