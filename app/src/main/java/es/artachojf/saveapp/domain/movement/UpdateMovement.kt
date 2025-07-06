package es.artachojf.saveapp.domain.movement

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.movement.model.MovementError
import es.artachojf.saveapp.domain.movement.model.MovementInput
import javax.inject.Inject

class UpdateMovement @Inject constructor(
    private val repository: MovementRepository
) {
    suspend operator fun invoke(movement: MovementInput): Result<Unit, MovementError> {
        return repository.updateMovement(movement)
    }
}