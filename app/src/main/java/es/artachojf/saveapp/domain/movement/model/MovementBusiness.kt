package es.artachojf.saveapp.domain.movement.model

import kotlinx.datetime.LocalDateTime

data class MovementBusiness(
    val id: Int,
    val userId: String,
    val title: String,
    val description: String,
    val amount: Double,
    val timestamp: LocalDateTime,
    val categoryId: Int
)