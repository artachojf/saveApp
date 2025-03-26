package es.artachojf.saveapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.logout.Logout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logout: Logout
): ViewModel() {
    private val _uiState = MutableStateFlow<HomeUIState>(HomeUIState.Idle)
    val uiState: StateFlow<HomeUIState> get() = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeUIState.Idle
        )

    fun onLogout() {
        _uiState.update {
            HomeUIState.Loading
        }
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                when (logout()) {
                    is Result.Success -> {
                        HomeUIState.LogoutSuccess
                    }

                    is Result.Failure -> {
                        HomeUIState.Idle
                    }
                }
            }
        }
    }
}