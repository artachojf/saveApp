package es.artachojf.saveapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.logout.Logout
import es.artachojf.saveapp.domain.login.user.GetLoggedUser
import es.artachojf.saveapp.ui.di.DispatcherIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val logout: Logout,
    private val getLoggedUser: GetLoggedUser,
): ViewModel() {
    private val _uiState = MutableStateFlow<HomeUIState>(HomeUIState.Idle())
    val uiState: StateFlow<HomeUIState> = _uiState
        .onStart { retrieveLoggedUser() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeUIState.Idle()
        )

    fun onLogout() {
        _uiState.update {
            HomeUIState.Loading
        }
        viewModelScope.launch(dispatcher) {
            _uiState.update {
                when (logout()) {
                    is Result.Success -> {
                        HomeUIState.LogoutSuccess
                    }

                    is Result.Failure -> {
                        HomeUIState.Idle()
                    }
                }
            }
        }
    }

    private fun retrieveLoggedUser() {
        _uiState.update {
            HomeUIState.Loading
        }
        viewModelScope.launch(dispatcher) {
            val result = getLoggedUser()
            _uiState.update {
                val loggedUser = when (result) {
                    is Result.Success -> result.data?.email

                    is Result.Failure -> null
                }
                HomeUIState.Idle(
                    loggedUser = loggedUser
                )
            }
        }
    }
}