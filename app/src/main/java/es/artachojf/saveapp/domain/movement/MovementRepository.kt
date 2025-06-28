package es.artachojf.saveapp.domain.movement

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.movement.model.MovementBusiness
import es.artachojf.saveapp.domain.movement.model.MovementError

interface MovementRepository {
    suspend fun getMovements(): Result<List<MovementBusiness>, MovementError>
}