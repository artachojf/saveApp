package es.artachojf.saveapp.data.movement.model

import es.artachojf.saveapp.core.utils.toLocalDateTime
import es.artachojf.saveapp.domain.movement.model.MovementBusiness
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovementResponse(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("amount") val amount: Double,
    @SerialName("timestamp") val timestamp: Instant,
    @SerialName("category_id") val categoryId: Int
)

fun MovementResponse.toDomain(): MovementBusiness {
    return MovementBusiness(
        id = id,
        userId = userId,
        title = title,
        description = description,
        amount = amount,
        timestamp = timestamp.toLocalDateTime(),
        categoryId = categoryId
    )
}