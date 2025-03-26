package es.artachojf.saveapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import es.artachojf.saveapp.ui.home.HomeScreen
import es.artachojf.saveapp.ui.login.LoginScreen

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
            HomeScreen(
                navigateToLogin = {
                    navController.navigate(
                        route = Login,
                        navOptions = navOptions {
                            popUpTo<Home> {
                                inclusive = true
                            }
                        }
                    )
                }
            )
        }
    }
}