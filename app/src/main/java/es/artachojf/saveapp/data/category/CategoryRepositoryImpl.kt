package es.artachojf.saveapp.data.category

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.core.map
import es.artachojf.saveapp.data.category.model.toDomain
import es.artachojf.saveapp.domain.category.CategoryRepository
import es.artachojf.saveapp.domain.category.model.CategoryBusiness
import es.artachojf.saveapp.domain.category.model.CategoryError

class CategoryRepositoryImpl(
    private val categoryDataSource: CategorySupabaseDataSource
) : CategoryRepository {
    override suspend fun getCategories(): Result<List<CategoryBusiness>, CategoryError> {
        return categoryDataSource.getAllCategories()
            .map({
                it.map { it.toDomain() }
            }) {
                it
            }
    }
}