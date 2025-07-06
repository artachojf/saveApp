package es.artachojf.saveapp.domain.movement

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.movement.model.MovementBusiness
import es.artachojf.saveapp.domain.movement.model.MovementError
import es.artachojf.saveapp.domain.movement.model.MovementInput

interface MovementRepository {
    suspend fun getMovements(): Result<List<MovementBusiness>, MovementError>
    suspend fun getMovement(id: Int): Result<MovementBusiness, MovementError>
    suspend fun insertMovement(movement: MovementInput): Result<Unit, MovementError>
    suspend fun updateMovement(movement: MovementInput): Result<Unit, MovementError>
}