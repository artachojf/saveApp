package es.artachojf.saveapp.ui.login.model

import es.artachojf.saveapp.domain.login.LoginError

sealed interface LoginUIEvent {
    data object Idle : LoginUIEvent
    data object LoginSuccess : LoginUIEvent
    data class Error(val error: LoginError) : LoginUIEvent
}