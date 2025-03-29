package es.artachojf.saveapp.ui.login

import es.artachojf.saveapp.domain.login.LoginError

sealed class LoginUIState {
    object Loading : LoginUIState()
    object Success : LoginUIState()
    data class Idle(val error: LoginError? = null) : LoginUIState()
}