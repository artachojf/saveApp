package es.artachojf.saveapp.ui.home.model

sealed interface HomeIntent {
    data object OnLogoutClick : HomeIntent
    data object OnGoToMovementForm : HomeIntent
    data class OnGoToMovementDetail(val movementId: Int) : HomeIntent
}