package es.artachojf.saveapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.artachojf.saveapp.R

@Composable
fun HomeScreen(
    navigateToLogin: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is HomeUIState.LogoutSuccess -> {
                navigateToLogin()
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
            is HomeUIState.Loading -> CircularProgressIndicator()

            is HomeUIState.Idle -> IdleHomeScreen(
                uiState = uiState as HomeUIState.Idle,
                onLogout = viewModel::onLogout,
            )

            else -> {}
        }
    }
}

@Composable
fun IdleHomeScreen(
    uiState: HomeUIState.Idle,
    onLogout: () -> Unit,
) {
    AnimatedVisibility(visible = uiState.loggedUser != null) {
        Text(text = "Hello ${uiState.loggedUser}")
    }
    Spacer(modifier = Modifier.height(4.dp))
    Button(onClick = { onLogout() }) {
        Text(text = stringResource(id = R.string.logout_button))
    }
}