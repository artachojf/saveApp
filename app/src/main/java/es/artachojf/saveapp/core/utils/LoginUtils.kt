package es.artachojf.saveapp.core.utils

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import es.artachojf.saveapp.BuildConfig
import es.artachojf.saveapp.domain.login.login.model.LoginMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

object LoginUtils {
    fun generateNonce(): Pair<String, String> {
        val rawNonce = UUID.randomUUID().toString()
        val byte = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(byte)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }
        return Pair(rawNonce, hashedNonce)
    }

    fun generateGoogleCredentialRequest(
        hashedNonce: String
    ): GetCredentialRequest {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
            .setAutoSelectEnabled(false)
            .setNonce(hashedNonce)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    fun launchGoogleCredentialManager(
        context: Context,
        coroutine: CoroutineScope,
        onLogin: (LoginMethod.GoogleLogin) -> Unit
    ) {
        val credentialManager = CredentialManager.create(context)

        val (rawNonce, hashedNonce) = generateNonce()
        val request = generateGoogleCredentialRequest(hashedNonce)

        coroutine.launch(Dispatchers.IO) {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                onLogin(LoginMethod.GoogleLogin(result, rawNonce))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}