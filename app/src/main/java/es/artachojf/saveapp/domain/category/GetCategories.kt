package es.artachojf.saveapp.domain.category

import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.category.model.CategoryBusiness
import es.artachojf.saveapp.domain.category.model.CategoryError
import javax.inject.Inject

class GetCategories @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): Result<List<CategoryBusiness>, CategoryError> {
        return repository.getCategories()
    }
}