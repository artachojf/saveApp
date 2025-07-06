package es.artachojf.saveapp.ui.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import es.artachojf.saveapp.R
import es.artachojf.saveapp.ui.home.model.HomeIntent
import es.artachojf.saveapp.ui.home.model.HomeUIEvent
import es.artachojf.saveapp.ui.home.model.HomeUIState
import es.artachojf.saveapp.ui.home.model.MovementViewEntity
import es.artachojf.saveapp.ui.theme.homeIncomeBackgroundColor
import es.artachojf.saveapp.ui.theme.homeIncomeIconBackgroundColor
import es.artachojf.saveapp.ui.theme.homeIncomeIconColor
import es.artachojf.saveapp.ui.theme.homeOutcomeBackgroundColor
import es.artachojf.saveapp.ui.theme.homeOutcomeIconBackgroundColor
import es.artachojf.saveapp.ui.theme.homeOutcomeIconColor
import es.artachojf.saveapp.ui.utils.getStringResource
import kotlinx.datetime.LocalDateTime

@Composable
fun HomeScreenRoot(
    navigateToLogin: () -> Unit,
    navigateToMovementForm: (Int?) -> Unit,
    navigateToMovementDetail: (Int) -> Unit,
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

    HomeScreen(
        state = uiState
    ) { intent ->
        when (intent) {
            is HomeIntent.OnGoToMovementForm -> navigateToMovementForm(intent.movementId)

            is HomeIntent.OnGoToMovementDetail -> navigateToMovementDetail(intent.movementId)

            else -> viewModel.onIntent(intent)
        }
    }
}

@Composable
fun HomeScreen(
    state: HomeUIState,
    onIntent: (HomeIntent) -> Unit
) {
    if (state.isLoading)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    else
        MovementsList(
            movements = state.movements,
            accountBalance = state.accountBalance,
            onIntent = onIntent
        )
}

@Composable
fun MovementsList(
    movements: List<MovementViewEntity>?,
    accountBalance: String?,
    onIntent: (HomeIntent) -> Unit
) {
    if (movements == null)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.home_empty_state),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues()),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onIntent(HomeIntent.OnGoToMovementForm())
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = LocalContext.current.getString(R.string.add_movement_content_description)
                    )
                }
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(5.dp),
            ) {
                AnimatedVisibility(
                    visible = accountBalance != null,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .align(Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = stringResource(R.string.home_account_balance_content_description),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(
                                R.string.home_account_balance_value,
                                accountBalance ?: ""
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        count = movements.size,
                        key = { movements[it].id }
                    ) {
                        MovementCard(movements[it], onIntent)
                    }
                }
            }
        }
    }
}

@Composable
fun MovementCard(
    item: MovementViewEntity,
    onIntent: (HomeIntent) -> Unit
) {
    val backgroundColor = if (item.amount >= 0)
        MaterialTheme.colorScheme.homeIncomeBackgroundColor
    else
        MaterialTheme.colorScheme.homeOutcomeBackgroundColor

    val iconBackgroundColor = if (item.amount >= 0)
        MaterialTheme.colorScheme.homeIncomeIconBackgroundColor
    else
        MaterialTheme.colorScheme.homeOutcomeIconBackgroundColor

    val iconColor = if (item.amount >= 0)
        MaterialTheme.colorScheme.homeIncomeIconColor
    else
        MaterialTheme.colorScheme.homeOutcomeIconColor

    val icon = if (item.amount >= 0)
        Icons.Default.KeyboardArrowUp
    else
        Icons.Default.KeyboardArrowDown

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = MaterialTheme.shapes.medium)
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.medium
            )
            .clickable {
                onIntent(HomeIntent.OnGoToMovementDetail(item.id))
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = iconBackgroundColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = item.description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = item.timestamp.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        Text(
            text = item.amount.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

@Preview
@Composable
fun preview() {
    val list = listOf(
        MovementViewEntity(
            id = 0,
            userId = "user",
            title = "Titulo 1",
            description = "Description 1",
            amount = 9.9,
            categoryId = 4,
            timestamp = LocalDateTime(2025, 5, 25, 23, 5)
        ),
        MovementViewEntity(
            id = 1,
            userId = "user",
            title = "Titulo 2",
            description = "Description 2",
            amount = -23.56,
            categoryId = 4,
            timestamp = LocalDateTime(2025, 5, 25, 23, 5)
        )
    )
    MovementsList(list, "23.67", {})
}