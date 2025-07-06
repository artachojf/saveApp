package es.artachojf.saveapp.domain.category.model

data class CategoryBusiness(
    val id: Int,
    val userId: Int?,
    val name: String,
    val color: String
)