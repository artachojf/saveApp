package es.artachojf.saveapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.LoginError
import es.artachojf.saveapp.domain.login.logout.Logout
import es.artachojf.saveapp.domain.login.user.GetLoggedUser
import es.artachojf.saveapp.domain.movement.GetMovements
import es.artachojf.saveapp.ui.di.DispatcherIO
import es.artachojf.saveapp.ui.home.model.HomeIntent
import es.artachojf.saveapp.ui.home.model.HomeUIEvent
import es.artachojf.saveapp.ui.home.model.HomeUIState
import es.artachojf.saveapp.ui.home.model.toPresentation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val logout: Logout,
    private val getLoggedUser: GetLoggedUser,
    private val getMovements: GetMovements
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState
        .onStart { getAllMovements() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeUIState()
        )

    private val _uiEvent: Channel<HomeUIEvent> = Channel()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.OnLogoutClick -> onLogout()
            else -> {}
        }
    }

    private fun onLogout() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(dispatcher) {
            when (logout()) {
                is Result.Success -> {
                    _uiEvent.send(HomeUIEvent.LogoutSuccess)
                }

                is Result.Failure -> {
                    _uiEvent.send(HomeUIEvent.Error(LoginError.LogoutError))
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun retrieveLoggedUser() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(dispatcher) {
            val loggedUser = when (val result = getLoggedUser()) {
                is Result.Success -> result.data?.email

                is Result.Failure -> {
                    _uiEvent.send(HomeUIEvent.Error(result.error))
                    null
                }
            }
            _uiState.update { it.copy(isLoading = false, loggedUser = loggedUser) }
        }
    }

    private fun getAllMovements() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(dispatcher) {
            val list = when (val result = getMovements()) {
                is Result.Success -> {
                    result.data
                }

                is Result.Failure -> {
                    _uiEvent.send(HomeUIEvent.Error(LoginError.GenericLoginError)) //TODO cambiar esto
                    emptyList()
                }
            }
            val accountBalance = round(list.sumOf { it.amount } * 100) / 100f
            _uiState.update {
                it.copy(
                    isLoading = false,
                    movements = list.map { it.toPresentation() },
                    accountBalance = accountBalance.toString()
                )
            }
        }
    }
}