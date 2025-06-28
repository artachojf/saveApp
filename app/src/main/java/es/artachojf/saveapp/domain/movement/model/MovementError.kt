package es.artachojf.saveapp.domain.movement.model

sealed interface MovementError {
    data object GenericError : MovementError
    data object GetError : MovementError
}