package es.artachojf.saveapp.ui.movement.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.artachojf.saveapp.core.Result
import es.artachojf.saveapp.domain.category.GetCategories
import es.artachojf.saveapp.domain.movement.MovementUseCases
import es.artachojf.saveapp.domain.movement.model.MovementInput
import es.artachojf.saveapp.ui.category.model.toPresentation
import es.artachojf.saveapp.ui.di.DispatcherIO
import es.artachojf.saveapp.ui.movement.form.model.MovementFormIntent
import es.artachojf.saveapp.ui.movement.form.model.MovementFormUIEvent
import es.artachojf.saveapp.ui.movement.form.model.MovementFormUIState
import es.artachojf.saveapp.ui.movement.form.model.MovementType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class MovementFormViewModel @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    private val movementUseCase: MovementUseCases,
    private val getCategories: GetCategories
) : ViewModel() {

    private val movementId: Int? = savedStateHandle["movementId"]

    private val _uiState = MutableStateFlow(MovementFormUIState())
    val uiState: StateFlow<MovementFormUIState> = _uiState
        .onStart { loadInitialData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = MovementFormUIState()
        )

    private val _uiEvent: Channel<MovementFormUIEvent> = Channel()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onIntent(intent: MovementFormIntent) {
        when (intent) {
            is MovementFormIntent.OnIncomeClicked -> {
                _uiState.value = _uiState.value.copy(
                    movementType = MovementType.INCOME
                )
            }

            is MovementFormIntent.OnExpenseClicked -> {
                _uiState.value = _uiState.value.copy(
                    movementType = MovementType.EXPENSE
                )
            }

            is MovementFormIntent.OnAmountChanged -> {
                val newAmount = intent.amount.replace(",", ".")

                val dotIndex = newAmount.indexOf('.')
                val decimalsAreRight =
                    newAmount.count { it == '.' } <= 1 && (dotIndex == -1 || newAmount.length - dotIndex - 1 <= 2)
                val onlyNumbers = newAmount.all { it.isDigit() || it == '.' }
                if (onlyNumbers && newAmount.firstOrNull() != '.' && decimalsAreRight)
                    _uiState.value = _uiState.value.copy(
                        amount = newAmount
                    )
            }

            is MovementFormIntent.OnTitleChanged -> {
                _uiState.value = _uiState.value.copy(
                    title = intent.title
                )
            }

            is MovementFormIntent.OnCategorySelected -> {
                _uiState.value = _uiState.value.copy(
                    categoryId = intent.categoryId
                )
            }

            is MovementFormIntent.OnDateSelected -> {
                _uiState.value = _uiState.value.copy(
                    date = intent.date
                )
            }

            is MovementFormIntent.OnDescriptionChanged -> {
                _uiState.value = _uiState.value.copy(
                    description = intent.description
                )
            }

            is MovementFormIntent.OnSaveMovement -> {
                val amount = _uiState.value.amount?.toDoubleOrNull()
                    ?.times((_uiState.value.movementType?.factor ?: 1))
                val input = MovementInput(
                    id = movementId,
                    title = _uiState.value.title ?: "",
                    description = _uiState.value.description ?: "",
                    amount = amount ?: 0.0,
                    timestamp = Instant.fromEpochMilliseconds(_uiState.value.date ?: 0),
                    categoryId = _uiState.value.categoryId ?: 0
                )
                if (movementId == null)
                    saveMovement(input)
                else
                    updateMovement(input)
            }

            else -> {}
        }
    }

    private fun loadInitialData() {
        _uiState.value = _uiState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(dispatcher) {
            when (val result = getCategories()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        categories = result.data.map { it.toPresentation() }
                    )
                    if (movementId != null)
                        getMovement(movementId)
                    else
                        _uiState.value = _uiState.value.copy(
                            isLoading = false
                        )
                }

                is Result.Failure -> {
                    _uiEvent.send(MovementFormUIEvent.OnGetCategoriesFailure)
                }
            }
        }
    }

    private fun getMovement(id: Int) {
        _uiState.value = _uiState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(dispatcher) {
            when (val result = movementUseCase.getMovement(id)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        title = result.data.title,
                        description = result.data.description,
                        amount = result.data.amount.absoluteValue.toString(),
                        date = result.data.timestamp.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(), //TODO revisar si lo queremos en UTC o no
                        categoryId = result.data.categoryId,
                        movementType = if (result.data.amount > 0) MovementType.INCOME else MovementType.EXPENSE
                    )
                }

                is Result.Failure -> {
                    _uiEvent.send(MovementFormUIEvent.OnGetFailure)
                }
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false
            )
        }
    }

    private fun saveMovement(input: MovementInput) {
        _uiState.value = _uiState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(dispatcher) {
            when (movementUseCase.addMovement(input)) {
                is Result.Success -> {
                    _uiEvent.send(MovementFormUIEvent.OnSaveSuccess)
                }

                is Result.Failure -> {
                    _uiEvent.send(MovementFormUIEvent.OnSaveFailure)
                }
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false
            )
        }
    }

    private fun updateMovement(input: MovementInput) {
        _uiState.value = _uiState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(dispatcher) {
            when (movementUseCase.updateMovement(input)) {
                is Result.Success -> {
                    _uiEvent.send(MovementFormUIEvent.OnUpdateSuccess)
                }

                is Result.Failure -> {
                    _uiEvent.send(MovementFormUIEvent.OnUpdateFailure)
                }
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false
            )
        }
    }
}