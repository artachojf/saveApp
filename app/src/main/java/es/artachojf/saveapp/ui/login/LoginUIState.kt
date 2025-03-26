package es.artachojf.saveapp.ui.login

sealed class LoginUIState {
    object Loading : LoginUIState()
    object Success : LoginUIState()
    data class Idle(val error: String? = null) : LoginUIState()
}