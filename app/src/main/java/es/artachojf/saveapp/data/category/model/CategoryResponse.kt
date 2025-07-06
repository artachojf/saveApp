package es.artachojf.saveapp.data.category.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import es.artachojf.saveapp.domain.category.model.CategoryBusiness

@Serializable
data class CategoryResponse(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: Int?,
    @SerialName("name") val name: String,
    @SerialName("color") val color: String
)

fun CategoryResponse.toDomain(): CategoryBusiness {
    return CategoryBusiness(
        id = id,
        userId = userId,
        name = name,
        color = color
    )
}