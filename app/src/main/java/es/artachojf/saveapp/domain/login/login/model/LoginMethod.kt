package es.artachojf.saveapp.domain.login.login.model

import androidx.credentials.GetCredentialResponse

sealed class LoginMethod {
    data class GoogleLogin(
        val result: GetCredentialResponse,
        val rawNonce: String
    ): LoginMethod()

    object AnonymousLogin : LoginMethod()
}