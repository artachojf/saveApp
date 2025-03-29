package es.artachojf.saveapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.login.Login
import es.artachojf.saveapp.domain.login.login.model.LoginMethod
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
class LoginViewModel @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val loginUseCase: Login,
    private val getLoggedUser: GetLoggedUser
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUIState>(LoginUIState.Idle())
    val uiState: StateFlow<LoginUIState> = _uiState
        .onStart { retrieveLoggedUser() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = LoginUIState.Idle()
        )

    fun login(method: LoginMethod) {
        _uiState.update {
            LoginUIState.Loading
        }
        viewModelScope.launch(dispatcher) {
            val result = loginUseCase(method)
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        LoginUIState.Success
                    }
                }

                is Result.Failure -> {
                    _uiState.update {
                        LoginUIState.Idle(result.error)
                    }
                }
            }
        }
    }

    private fun retrieveLoggedUser() {
        _uiState.update {
            LoginUIState.Loading
        }
        viewModelScope.launch(dispatcher) {
            val result = getLoggedUser()
            _uiState.update {
                when (result) {
                    is Result.Success -> {
                        if (result.data == null)
                            LoginUIState.Idle()
                        else
                            LoginUIState.Success
                    }

                    is Result.Failure -> {
                        LoginUIState.Idle(result.error)
                    }
                }
            }
        }
    }
}