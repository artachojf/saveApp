package es.artachojf.saveapp.domain.login.google.model

import androidx.credentials.GetCredentialResponse

data class GoogleLoginInput(
    val result: GetCredentialResponse,
    val rawNonce: String
)
