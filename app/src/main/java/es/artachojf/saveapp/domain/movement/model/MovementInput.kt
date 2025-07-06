package es.artachojf.saveapp.domain.movement.model

import kotlinx.datetime.Instant

data class MovementInput(
    val id: Int? = null,
    val title: String,
    val description: String,
    val amount: Double,
    val timestamp: Instant,
    val categoryId: Int
)