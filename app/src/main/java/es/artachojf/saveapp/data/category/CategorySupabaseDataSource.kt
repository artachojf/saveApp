package es.artachojf.saveapp.data.category

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.data.category.model.CategoryResponse
import es.artachojf.saveapp.domain.category.model.CategoryError
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class CategorySupabaseDataSource @Inject constructor(
    private val supabase: SupabaseClient
) {
    companion object {
        const val CATEGORY_TABLE_NAME = "category"
    }

    suspend fun getAllCategories(): Result<List<CategoryResponse>, CategoryError> {
        return try {
            Result.Success(
                supabase.from(CATEGORY_TABLE_NAME)
                    .select()
                    .decodeList<CategoryResponse>()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Failure(CategoryError.GetError)
        }
    }
}