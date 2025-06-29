package es.artachojf.saveapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
    surface = DarkGray800,
    onSurface = Color.White,
    secondary = Color.Black,
    onSecondary = DarkGray400
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = LightGray50,
    onSurface = Color.Black,
    secondary = DarkGray400,
    onSecondary = Color.Black
)

val ColorScheme.homeIncomeBackgroundColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Green50 else Green50

val ColorScheme.homeIncomeIconBackgroundColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Green200 else Green200

val ColorScheme.homeIncomeIconColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Green700 else Green700

val ColorScheme.homeOutcomeBackgroundColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Red50 else Red50

val ColorScheme.homeOutcomeIconBackgroundColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Red200 else Red200

val ColorScheme.homeOutcomeIconColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Red700 else Red700

@Composable
fun SaveAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}