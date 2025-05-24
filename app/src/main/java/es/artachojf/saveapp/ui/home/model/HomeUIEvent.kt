package es.artachojf.saveapp.ui.home.model

import es.artachojf.saveapp.domain.login.LoginError

sealed interface HomeUIEvent {
    data object Idle : HomeUIEvent
    data object LogoutSuccess : HomeUIEvent
    data class Error(val error: LoginError) : HomeUIEvent
}