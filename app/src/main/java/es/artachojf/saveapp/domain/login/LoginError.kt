package es.artachojf.saveapp.domain.login

sealed class LoginError {
    object GenericLoginError : LoginError()

    object GetLoggedUserError : LoginError()
}