package es.artachojf.saveapp.data.movement.model

import es.artachojf.saveapp.domain.movement.model.MovementInput
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovementRequest(
    @SerialName("id") val id: Int? = null,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("amount") val amount: Double,
    @SerialName("timestamp") val timestamp: Instant,
    @SerialName("category_id") val categoryId: Int
)

fun MovementInput.toRequest(): MovementRequest {
    return MovementRequest(
        id = id,
        title = title,
        description = description,
        amount = amount,
        timestamp = timestamp,
        categoryId = categoryId
    )
}