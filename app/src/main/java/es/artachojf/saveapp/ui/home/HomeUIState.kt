package es.artachojf.saveapp.ui.home

sealed class HomeUIState {
    object Loading : HomeUIState()
    object LogoutSuccess : HomeUIState()
    object Idle : HomeUIState()
}