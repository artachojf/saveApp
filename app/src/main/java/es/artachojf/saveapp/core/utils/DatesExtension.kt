package es.artachojf.saveapp.core.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toLocalDateTime(): LocalDateTime {
    val timeZone = TimeZone.currentSystemDefault()
    return this.toLocalDateTime(timeZone)
}