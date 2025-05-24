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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import es.artachojf.saveapp.R
import es.artachojf.saveapp.core.utils.LoginUtils
import es.artachojf.saveapp.domain.login.LoginError
import es.artachojf.saveapp.domain.login.login.model.LoginMethod

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
                    val resource = when (it) {
                        is LoginError.GenericLoginError -> R.string.generic_login_error

                        is LoginError.GetLoggedUserError -> R.string.get_logged_user_error
                    }
                    Toast.makeText(context, context.getString(resource), Toast.LENGTH_LONG).show()
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
                    login = { method ->
                        viewModel.login(method)
                    }
                )
            }

            else -> {}
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