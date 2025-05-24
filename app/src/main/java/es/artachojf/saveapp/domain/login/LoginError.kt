package es.artachojf.saveapp.domain.login

sealed interface LoginError {
    data object GenericLoginError : LoginError
    data object GetLoggedUserError : LoginError
    data object LogoutError : LoginError
}