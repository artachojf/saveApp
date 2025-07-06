package es.artachojf.saveapp.domain.movement

data class MovementUseCases(
    val getMovements: GetMovements,
    val getMovement: GetMovement,
    val addMovement: AddMovement,
    val updateMovement: UpdateMovement
)