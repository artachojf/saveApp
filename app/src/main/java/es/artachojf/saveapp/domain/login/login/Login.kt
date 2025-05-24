package es.artachojf.saveapp.domain.login.login

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.LoginError
import es.artachojf.saveapp.domain.login.login.model.LoginMethod
import javax.inject.Inject

class Login @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(option: LoginMethod): Result<Unit, LoginError> {
        return when (option) {
            is LoginMethod.GoogleLogin -> {
                repository.loginWithGoogle(option.result, option.rawNonce)
            }

            is LoginMethod.AnonymousLogin -> {
                repository.loginAnonymously()
            }
        }
    }
}