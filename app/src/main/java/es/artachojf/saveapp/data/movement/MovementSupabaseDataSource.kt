package es.artachojf.saveapp.data.movement

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.data.movement.model.MovementRequest
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
        const val MOVEMENT_ID_COLUMN = "id"
        const val MOVEMENT_TITLE_COLUMN = "title"
        const val MOVEMENT_DESCRIPTION_COLUMN = "description"
        const val MOVEMENT_TIMESTAMP_COLUMN = "timestamp"
        const val MOVEMENT_CATEGORY_ID_COLUMN = "category_id"
        const val MOVEMENT_AMOUNT_COLUMN = "amount"
    }

    suspend fun getAllMovements(): Result<List<MovementResponse>, MovementError> {
        return try {
            Result.Success(
                supabase.from(MOVEMENT_TABLE_NAME)
                    .select()
                    .decodeList<MovementResponse>()
            )
        } catch (_: Exception) {
            Result.Failure(MovementError.GetError)
        }
    }

    suspend fun getMovement(id: Int): Result<MovementResponse, MovementError> {
        return try {
            Result.Success(
                supabase.from(MOVEMENT_TABLE_NAME)
                    .select {
                        filter {
                            eq(MOVEMENT_ID_COLUMN, id)
                        }
                    }
                    .decodeSingle()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Failure(MovementError.GetError)
        }
    }

    suspend fun insertMovement(movement: MovementRequest): Result<Unit, MovementError> {
        return try {
            supabase.from(MOVEMENT_TABLE_NAME)
                .insert(movement)
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Failure(MovementError.InsertError)
        }
    }

    suspend fun updateMovement(movement: MovementRequest): Result<Unit, MovementError> {
        return try {
            movement.id?.let { id ->
                supabase.from(MOVEMENT_TABLE_NAME)
                    .update({
                        set(MOVEMENT_TITLE_COLUMN, movement.title)
                        set(MOVEMENT_DESCRIPTION_COLUMN, movement.description)
                        set(MOVEMENT_TIMESTAMP_COLUMN, movement.timestamp)
                        set(MOVEMENT_CATEGORY_ID_COLUMN, movement.categoryId)
                        set(MOVEMENT_AMOUNT_COLUMN, movement.amount)
                    }) {
                        filter {
                            eq(MOVEMENT_ID_COLUMN, id)
                        }
                    }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Failure(MovementError.UpdateError)
        }
    }
}