package es.artachojf.saveapp.data.movement

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.core.map
import es.artachojf.saveapp.data.movement.model.toDomain
import es.artachojf.saveapp.domain.movement.model.MovementBusiness
import es.artachojf.saveapp.domain.movement.model.MovementError
import es.artachojf.saveapp.domain.movement.MovementRepository
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
}