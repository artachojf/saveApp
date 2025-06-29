package es.artachojf.saveapp.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import es.artachojf.saveapp.R
import es.artachojf.saveapp.core.utils.LoginUtils
import es.artachojf.saveapp.domain.login.login.model.LoginMethod
import es.artachojf.saveapp.ui.login.model.LoginUIEvent
import es.artachojf.saveapp.ui.utils.getStringResource

@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val uiEvent by viewModel.uiEvent.collectAsState(initial = LoginUIEvent.Idle)
    val context = LocalContext.current

    LaunchedEffect(uiEvent) {
        when (uiEvent) {
            is LoginUIEvent.LoginSuccess -> {
                navigateToHome()
            }

            is LoginUIEvent.Error -> {
                Toast.makeText(
                    context,
                    context.getString((uiEvent as LoginUIEvent.Error).error.getStringResource()),
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState.isLoading) {
            true -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)

            false -> {
                IdleLoginScreen(
                    login = { method ->
                        viewModel.login(method)
                    }
                )
            }
        }
    }
}

@Composable
fun IdleLoginScreen(
    login: (LoginMethod) -> Unit,
) {
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current

    val onGoogleLogin = {
        (login as? ((LoginMethod.GoogleLogin) -> Unit))?.let {
            LoginUtils.launchGoogleCredentialManager(
                context = context,
                coroutine = coroutine,
                onLogin = it
            )
        }
    }

    val onAnonymousLogin = {
        login(LoginMethod.AnonymousLogin)
    }

    Button(onClick = { onGoogleLogin() }) {
        Text(text = stringResource(id = R.string.google_button))
    }
    TextButton(onClick = { onAnonymousLogin() }) {
        Text(text = stringResource(id = R.string.anonymous_button))
    }
}