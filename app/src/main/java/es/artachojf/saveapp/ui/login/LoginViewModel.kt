package es.artachojf.saveapp.ui.login

import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.login.anonymous.AnonymousLogin
import es.artachojf.saveapp.domain.login.google.GoogleLogin
import es.artachojf.saveapp.domain.login.google.model.GoogleLoginInput
import es.artachojf.saveapp.domain.login.user.GetLoggedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleLogin: GoogleLogin,
    private val anonymousLogin: AnonymousLogin,
    private val getLoggedUser: GetLoggedUser
): ViewModel() {

    private val _uiState = MutableStateFlow<LoginUIState>(LoginUIState.Loading)
    val uiState: StateFlow<LoginUIState> = _uiState
        .onStart { retrieveLoggedUser() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = LoginUIState.Loading
        )

    fun loginWithGoogle(
        result: GetCredentialResponse,
        rawNonce: String
    ) {
        _uiState.update {
            LoginUIState.Loading
        }
        viewModelScope.launch(Dispatchers.IO) {
            val response = googleLogin(
                GoogleLoginInput(
                    result,
                    rawNonce
                )
            )

            when (response) {
                is Result.Success -> {
                    _uiState.update {
                        LoginUIState.Success
                    }
                }

                is Result.Failure -> {
                    _uiState.update {
                        LoginUIState.Idle("Login error!")
                    }
                }
            }
        }
    }

    fun loginAnonymously() {
        _uiState.update {
            LoginUIState.Loading
        }
        viewModelScope.launch(Dispatchers.IO) {
            when (anonymousLogin()) {
                is Result.Success -> {
                    _uiState.update {
                        LoginUIState.Success
                    }
                }

                is Result.Failure -> {
                    _uiState.update {
                        LoginUIState.Idle("Login error!")
                    }
                }
            }
        }
    }

    private fun retrieveLoggedUser() {
        viewModelScope.launch(Dispatchers.IO) {
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
                        LoginUIState.Idle("There was an error retrieving the logged user. Please login again.")
                    }
                }
            }
        }
    }
}