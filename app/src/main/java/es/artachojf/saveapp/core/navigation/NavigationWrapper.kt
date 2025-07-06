package es.artachojf.saveapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import es.artachojf.saveapp.ui.home.HomeScreenRoot
import es.artachojf.saveapp.ui.login.LoginScreen
import es.artachojf.saveapp.ui.movement.detail.MovementDetailScreen
import es.artachojf.saveapp.ui.movement.form.MovementFormScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(
                        route = Home,
                        navOptions = navOptions {
                            popUpTo<Login> {
                                inclusive = true
                            }
                        }
                    )
                }
            )
        }

        composable<Home> {
            HomeScreenRoot(
                navigateToLogin = {
                    navController.navigate(
                        route = Login,
                        navOptions = navOptions {
                            popUpTo<Home> {
                                inclusive = true
                            }
                        }
                    )
                },
                navigateToMovementForm = { movementId ->
                    navController.navigate(
                        route = MovementForm(movementId)
                    )
                },
                navigateToMovementDetail = {
                    navController.navigate(
                        route = MovementForm(it) //TODO acceso provisional a edicion
                    )
                }
            )
        }

        composable<MovementDetail> {
            MovementDetailScreen()
        }

        composable<MovementForm> {
            MovementFormScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}