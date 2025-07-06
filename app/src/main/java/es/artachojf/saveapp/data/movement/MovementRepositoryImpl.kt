package es.artachojf.saveapp.data.movement

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.core.map
import es.artachojf.saveapp.data.movement.model.toDomain
import es.artachojf.saveapp.data.movement.model.toRequest
import es.artachojf.saveapp.domain.movement.model.MovementBusiness
import es.artachojf.saveapp.domain.movement.model.MovementError
import es.artachojf.saveapp.domain.movement.MovementRepository
import es.artachojf.saveapp.domain.movement.model.MovementInput
import javax.inject.Inject

class MovementRepositoryImpl @Inject constructor(
    private val dataSource: MovementSupabaseDataSource
) : MovementRepository {
    override suspend fun getMovements(): Result<List<MovementBusiness>, MovementError> {
        return dataSource
            .getAllMovements()
            .map({ responseList ->
                responseList.map { it.toDomain() }
            }) {
                it
            }
    }

    override suspend fun getMovement(id: Int): Result<MovementBusiness, MovementError> {
        return dataSource.getMovement(id).map({
            it.toDomain()
        }) {
            it
        }
    }

    override suspend fun insertMovement(movement: MovementInput): Result<Unit, MovementError> {
        return dataSource.insertMovement(movement.toRequest())
    }

    override suspend fun updateMovement(movement: MovementInput): Result<Unit, MovementError> {
        return dataSource.updateMovement(movement.toRequest())
    }
}