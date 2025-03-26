package es.artachojf.saveapp.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialResponse
import androidx.hilt.navigation.compose.hiltViewModel
import es.artachojf.saveapp.core.utils.LoginUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUIState.Success -> {
                navigateToHome()
            }

            is LoginUIState.Idle -> {
                (uiState as LoginUIState.Idle).error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState) {
            is LoginUIState.Loading -> CircularProgressIndicator()

            is LoginUIState.Idle -> {
                IdleLoginScreen(
                    loginWithGoogle = { result, nonce ->
                        viewModel.loginWithGoogle(result, nonce)
                    },
                    loginAnonymously = {
                        viewModel.loginAnonymously()
                    }
                )
            }

            else -> {}
        }
    }
}

@Composable
fun IdleLoginScreen(
    loginWithGoogle: (GetCredentialResponse, String) -> Unit,
    loginAnonymously: () -> Unit
) {
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current

    val onGoogleLogin = {
        val credentialManager = CredentialManager.create(context)

        val (rawNonce, hashedNonce) = LoginUtils.generateNonce()
        val request = LoginUtils.generateGoogleCredentialRequest(hashedNonce)

        coroutine.launch(Dispatchers.IO) {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                loginWithGoogle(result, rawNonce)
            } catch (e: Exception) {
                e.printStackTrace()
                //TODO: Gestion excepciones
            }
        }
    }

    val onAnonymousLogin = {
        loginAnonymously()
    }

    Button(onClick = { onGoogleLogin() }) {
        Text(text = "Sign in with Google")
    }
    TextButton(onClick = { onAnonymousLogin() }) {
        Text(text = "Continue without login")
    }
}