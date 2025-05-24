package es.artachojf.saveapp.ui.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.artachojf.saveapp.R
import es.artachojf.saveapp.ui.home.model.HomeUIEvent
import es.artachojf.saveapp.ui.utils.getStringResource

@Composable
fun HomeScreen(
    navigateToLogin: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val uiEvent by viewModel.uiEvent.collectAsState(initial = HomeUIEvent.Idle)
    val context = LocalContext.current

    LaunchedEffect(uiEvent) {
        when (uiEvent) {
            is HomeUIEvent.LogoutSuccess -> {
                navigateToLogin()
            }

            is HomeUIEvent.Error -> {
                Toast.makeText(
                    context,
                    context.getString((uiEvent as HomeUIEvent.Error).error.getStringResource()),
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState.isLoading) {
            true -> CircularProgressIndicator()

            false -> IdleHomeScreen(
                loggedUser = uiState.loggedUser,
                onLogout = viewModel::onLogout,
            )
        }
    }
}

@Composable
fun IdleHomeScreen(
    loggedUser: String?,
    onLogout: () -> Unit,
) {
    AnimatedVisibility(visible = !loggedUser.isNullOrEmpty()) {
        Text(text = stringResource(id = R.string.logged_user, loggedUser ?: ""))
        Spacer(modifier = Modifier.size(20.dp))
    }
    Button(onClick = { onLogout() }) {
        Text(text = stringResource(id = R.string.logout_button))
    }
}