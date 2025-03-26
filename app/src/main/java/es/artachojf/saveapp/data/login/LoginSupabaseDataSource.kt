package es.artachojf.saveapp.data.login

import android.credentials.GetCredentialException
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import es.artachojf.saveapp.core.Result
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import io.github.jan.supabase.gotrue.user.UserInfo
import javax.inject.Inject

class LoginSupabaseDataSource @Inject constructor(
    private val supabase: SupabaseClient,
) {
    suspend fun loginWithGoogle(
        result: GetCredentialResponse,
        rawNonce: String
    ): Result<Unit, Unit> {
        return try {
            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(result.credential.data)

            val googleIdToken = googleIdTokenCredential.idToken

            supabase.auth.signInWith(IDToken) {
                idToken = googleIdToken
                provider = Google
                nonce = rawNonce
            }
            Result.Success(Unit)
        } catch (e: GetCredentialException) {
            handleFailure(e)
        } catch (e: GoogleIdTokenParsingException) {
            handleFailure(e)
        } catch (e: RestException) {
            handleFailure(e)
        } catch (e: Exception) {
            handleFailure(e)
        }
    }

    private fun handleFailure(e: Exception): Result<Nothing, Unit> {
        e.printStackTrace()
        return Result.Failure(Unit)
    }

    suspend fun loginAnonymously(): Result<Unit, Unit> {
        return try {
            supabase.auth.signInAnonymously()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Unit)
        }
    }

    suspend fun getLoggedUser(): Result<UserInfo?, Unit> {
        return try {
            supabase.auth.sessionManager.loadSession()?.user.let {
                Result.Success(it)
            }
        } catch (e: Exception) {
            Result.Failure(Unit)
        }
    }

    suspend fun logout(): Result<Unit, Unit> {
        return try {
            supabase.auth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Unit)
        }
    }
}