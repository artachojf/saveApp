package es.artachojf.saveapp.domain.movement

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.movement.model.MovementBusiness
import es.artachojf.saveapp.domain.movement.model.MovementError
import javax.inject.Inject

class GetMovements @Inject constructor(
    private val repository: MovementRepository
) {
    suspend operator fun invoke(): Result<List<MovementBusiness>, MovementError> {
        return repository.getMovements()
    }
}