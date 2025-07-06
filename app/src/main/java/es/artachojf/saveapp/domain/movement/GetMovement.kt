package es.artachojf.saveapp.domain.movement

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.movement.model.MovementBusiness
import es.artachojf.saveapp.domain.movement.model.MovementError
import javax.inject.Inject

class GetMovement @Inject constructor(
    private val repository: MovementRepository
) {
    suspend operator fun invoke(id: Int): Result<MovementBusiness, MovementError> {
        return repository.getMovement(id)
    }
}