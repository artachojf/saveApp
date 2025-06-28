package es.artachojf.saveapp.data.movement

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.data.movement.model.MovementResponse
import es.artachojf.saveapp.domain.movement.model.MovementError
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class MovementSupabaseDataSource @Inject constructor(
    private val supabase: SupabaseClient
) {
    companion object {
        const val MOVEMENT_TABLE_NAME = "movement"
    }

    suspend fun getAllMovements(): Result<List<MovementResponse>, MovementError> {
        return try {
            Result.Success(
                supabase.from(MOVEMENT_TABLE_NAME)
                    .select()
                    .decodeList<MovementResponse>()
            )
        } catch (e: Exception) {
            Result.Failure(MovementError.GetError)
        }
    }
}