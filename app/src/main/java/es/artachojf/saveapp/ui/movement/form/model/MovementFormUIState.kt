package es.artachojf.saveapp.ui.movement.form.model

import es.artachojf.saveapp.ui.category.model.CategoryViewEntity

data class MovementFormUIState(
    val isLoading: Boolean = false,
    val categories: List<CategoryViewEntity>? = null,
    val movementType: MovementType? = null,
    val amount: String? = null,
    val title: String? = null,
    val description: String? = null,
    val categoryId: Int? = null,
    val date: Long? = null
)

enum class MovementType(val factor: Int) {
    INCOME(1),
    EXPENSE(-1)
}