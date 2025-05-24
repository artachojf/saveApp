package es.artachojf.saveapp.ui.utils

import es.artachojf.saveapp.R
import es.artachojf.saveapp.domain.login.LoginError

fun LoginError.getStringResource(): Int {
    return when (this) {
        is LoginError.GenericLoginError -> R.string.generic_login_error
        is LoginError.GetLoggedUserError -> R.string.get_logged_user_error
        is LoginError.LogoutError -> R.string.logout_error
    }
}