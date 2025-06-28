package es.artachojf.saveapp.ui.home.model

sealed interface HomeIntent {
    data object OnLogoutClick : HomeIntent
}