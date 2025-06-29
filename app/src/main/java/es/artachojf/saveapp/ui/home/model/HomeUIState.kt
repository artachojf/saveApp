package es.artachojf.saveapp.ui.home.model

data class HomeUIState(
    val isLoading: Boolean = false,
    val loggedUser: String? = null,
    val movements: List<MovementViewEntity>? = null,
    val accountBalance: String? = null
)