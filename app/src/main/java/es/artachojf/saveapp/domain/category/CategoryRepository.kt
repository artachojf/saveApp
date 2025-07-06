package es.artachojf.saveapp.domain.category

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.category.model.CategoryBusiness
import es.artachojf.saveapp.domain.category.model.CategoryError

interface CategoryRepository {
    suspend fun getCategories(): Result<List<CategoryBusiness>, CategoryError>
}