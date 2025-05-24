package es.artachojf.saveapp.ui.home

import es.artachojf.saveapp.domain.login.LoginError

sealed class HomeUIState {
    object Loading : HomeUIState()
    object LogoutSuccess : HomeUIState()
    data class Idle(
        val loggedUser: String? = null,
        val error: LoginError? = null,
        val loginUpdateSuccess: Boolean = false
    ) : HomeUIState()
}