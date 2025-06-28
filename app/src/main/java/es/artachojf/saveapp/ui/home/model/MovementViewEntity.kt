package es.artachojf.saveapp.ui.home.model

import es.artachojf.saveapp.domain.movement.model.MovementBusiness
import kotlinx.datetime.LocalDateTime

data class MovementViewEntity(
    val id: Int,
    val userId: String,
    val title: String,
    val description: String,
    val amount: Double,
    val timestamp: LocalDateTime,
    val categoryId: Int
)

fun MovementBusiness.toPresentation(): MovementViewEntity {
    return MovementViewEntity(
        id = id,
        userId = userId,
        title = title,
        description = description,
        amount = amount,
        timestamp = timestamp,
        categoryId = categoryId
    )
}