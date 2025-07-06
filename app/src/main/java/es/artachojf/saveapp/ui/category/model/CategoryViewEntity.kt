package es.artachojf.saveapp.ui.category.model

import es.artachojf.saveapp.domain.category.model.CategoryBusiness

data class CategoryViewEntity(
    val id: Int,
    val name: String,
    val color: String
)

fun CategoryBusiness.toPresentation(): CategoryViewEntity {
    return CategoryViewEntity(
        id = id,
        name = name,
        color = color
    )
}