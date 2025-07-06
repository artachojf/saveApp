package es.artachojf.saveapp.ui.movement.form

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.artachojf.saveapp.R
import es.artachojf.saveapp.ui.movement.form.model.MovementFormIntent
import es.artachojf.saveapp.ui.movement.form.model.MovementFormUIEvent
import es.artachojf.saveapp.ui.movement.form.model.MovementFormUIState
import es.artachojf.saveapp.ui.movement.form.model.MovementType
import es.artachojf.saveapp.ui.theme.homeIncomeIconBackgroundColor
import es.artachojf.saveapp.ui.theme.homeOutcomeIconBackgroundColor

@Composable
fun MovementFormScreen(
    navigateBack: () -> Unit,
    viewModel: MovementFormViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val uiEvent by viewModel.uiEvent.collectAsState(initial = MovementFormUIEvent.Idle)

    val context = LocalContext.current

    LaunchedEffect(uiEvent) {
        when (uiEvent) {
            is MovementFormUIEvent.OnGetCategoriesFailure -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.movement_form_get_categories_failure),
                    Toast.LENGTH_LONG
                ).show()
            }

            is MovementFormUIEvent.OnGetFailure -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.movement_form_get_movement_failure),
                    Toast.LENGTH_LONG
                ).show()
            }

            is MovementFormUIEvent.OnSaveSuccess -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.movement_form_save_movement_success),
                    Toast.LENGTH_LONG
                ).show()
                navigateBack()
            }

            is MovementFormUIEvent.OnSaveFailure -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.movement_form_save_movement_failure),
                    Toast.LENGTH_LONG
                ).show()
            }

            is MovementFormUIEvent.OnUpdateSuccess -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.movement_form_update_movement_success),
                    Toast.LENGTH_LONG
                ).show()
                navigateBack()
            }

            is MovementFormUIEvent.OnUpdateFailure -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.movement_form_update_movement_failure),
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {}
        }
    }

    MovementForm(
        state = uiState,
    ) { intent ->
        when (intent) {
            is MovementFormIntent.GoBack -> navigateBack()

            else -> viewModel.onIntent(intent)
        }
    }
}

@Composable
fun MovementForm(
    state: MovementFormUIState,
    onIntent: (MovementFormIntent) -> Unit,
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
        }
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues()),
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onIntent(MovementFormIntent.GoBack) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = LocalContext.current.getString(R.string.go_back_content_description),
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        modifier = Modifier.weight(1f),
                        text = LocalContext.current.getString(R.string.movement_form_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            },
            bottomBar = {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 10.dp,
                            vertical = 8.dp
                        ),
                    onClick = {
                        onIntent(MovementFormIntent.OnSaveMovement)
                    }
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val incomeBackgroundColor =
                        if (state.movementType == MovementType.INCOME) MaterialTheme.colorScheme.homeIncomeIconBackgroundColor
                        else MaterialTheme.colorScheme.surface

                    val expenseBackgroundColor =
                        if (state.movementType == MovementType.EXPENSE) MaterialTheme.colorScheme.homeOutcomeIconBackgroundColor
                        else MaterialTheme.colorScheme.surface

                    Box(
                        modifier = Modifier
                            .height(70.dp)
                            .weight(1f)
                            .background(
                                color = incomeBackgroundColor, shape = RoundedCornerShape(24.dp)
                            )
                            .clickable {
                                onIntent(MovementFormIntent.OnIncomeClicked)
                            }, contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(R.string.movement_form_income))
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    Box(
                        modifier = Modifier
                            .height(70.dp)
                            .weight(1f)
                            .background(
                                color = expenseBackgroundColor, shape = RoundedCornerShape(24.dp)
                            )
                            .clickable {
                                onIntent(MovementFormIntent.OnExpenseClicked)
                            }, contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(R.string.movement_form_expense))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.title?.toString() ?: "",
                    onValueChange = { onIntent(MovementFormIntent.OnTitleChanged(it)) },
                    label = { Text(text = stringResource(R.string.movement_form_title_field)) },
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(30.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.amount?.toString() ?: "",
                    onValueChange = { onIntent(MovementFormIntent.OnAmountChanged(it)) },
                    label = { Text(text = stringResource(R.string.movement_form_amount_field)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                Spacer(modifier = Modifier.height(30.dp))

                DropdownField(
                    modifier = Modifier.fillMaxWidth(),
                    uiState = state,
                    onCategorySelected = { onIntent(MovementFormIntent.OnCategorySelected(it)) },
                )

                Spacer(modifier = Modifier.height(30.dp))

                DatePickerField(
                    modifier = Modifier.fillMaxWidth(), value = state.date, onDateSelected = {
                        onIntent(MovementFormIntent.OnDateSelected(it))
                    })

                Spacer(modifier = Modifier.height(30.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.description?.toString() ?: "",
                    onValueChange = { onIntent(MovementFormIntent.OnDescriptionChanged(it)) },
                    label = { Text(text = stringResource(R.string.movement_form_description_field)) },
                    singleLine = false,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    modifier: Modifier = Modifier,
    uiState: MovementFormUIState,
    onCategorySelected: (Int?) -> Unit,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded, onExpandedChange = {
                if (enabled) expanded = !expanded
            }, modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = uiState.categories?.find { it.id == uiState.categoryId }?.name
                    ?: "Select a Category",
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                label = { Text("Category") },
                enabled = enabled,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown, contentDescription = null
                    )
                })

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(text = { Text("Select a Category") }, onClick = {
                    onCategorySelected(null)
                    expanded = false
                })
                uiState.categories?.forEach { category ->
                    DropdownMenuItem(text = { Text(category.name) }, onClick = {
                        onCategorySelected(category.id)
                        expanded = false
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    modifier: Modifier = Modifier, value: Long?, onDateSelected: (Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = value
    )
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value?.toString() ?: "", //TODO cambiar a texto
        onValueChange = {},
        label = { Text(text = stringResource(R.string.movement_form_date_field)) },
        singleLine = true,
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange, contentDescription = null
            )
        },
        interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            showDatePicker = true
                        }
                    }
                }
            })

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
                        showDatePicker = false
                    }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

@Composable
@Preview
fun preview() {
    MovementForm(
        state = MovementFormUIState(movementType = MovementType.EXPENSE),
    ) { }
}