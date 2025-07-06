package es.artachojf.saveapp.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Home

@Serializable
object MovementDetail

@Serializable
data class MovementForm(val movementId: Int?)