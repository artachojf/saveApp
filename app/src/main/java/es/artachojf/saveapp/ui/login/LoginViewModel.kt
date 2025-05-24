package es.artachojf.saveapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.login.Login
import es.artachojf.saveapp.domain.login.login.model.LoginMethod
import es.artachojf.saveapp.domain.login.user.GetLoggedUser
import es.artachojf.saveapp.ui.di.DispatcherIO
import es.artachojf.saveapp.ui.login.model.LoginUIEvent
import es.artachojf.saveapp.ui.login.model.LoginUIState
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

@HiltViewModel
class LoginViewModel @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val loginUseCase: Login,
    private val getLoggedUser: GetLoggedUser
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState
        .onStart { retrieveLoggedUser() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = LoginUIState()
        )

    private val _uiEvent: Channel<LoginUIEvent> = Channel()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun login(method: LoginMethod) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(dispatcher) {
            when (val result = loginUseCase(method)) {
                is Result.Success -> {
                    _uiEvent.send(LoginUIEvent.LoginSuccess)
                }

                is Result.Failure -> {
                    _uiEvent.send(LoginUIEvent.Error(result.error))
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun retrieveLoggedUser() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(dispatcher) {
            val isLoading = when (val result = getLoggedUser()) {
                is Result.Success -> {
                    if (result.data == null)
                        false
                    else {
                        _uiEvent.send(LoginUIEvent.LoginSuccess)
                        true
                    }
                }

                is Result.Failure -> {
                    _uiEvent.send(LoginUIEvent.Error(result.error))
                    false
                }
            }
            _uiState.update { it.copy(isLoading = isLoading) }
        }
    }
}