package es.artachojf.saveapp.ui.home.model

sealed interface HomeIntent {
    data object OnLogoutClick : HomeIntent
    data class OnGoToMovementForm(val movementId: Int? = null) : HomeIntent
    data class OnGoToMovementDetail(val movementId: Int) : HomeIntent
}