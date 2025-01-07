package com.myattheingi.wexchanger.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(
    primary = CustomBlue,
    onPrimary = Color.White,
    secondary = CustomYellow,
    onSecondary = Color.Black,
    tertiary = CustomBlue, // Optional, if you need a third accent color
    onTertiary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White,
    secondaryContainer = CustomYellow.copy(alpha = 0.1f), // Light background for secondary container
    onSecondaryContainer = CustomBlueDark,  // Text color on secondary container
)

private val DarkColorScheme = darkColorScheme(
    primary = CustomBlue,
    onPrimary = Color.White,
    secondary = CustomYellow,
    onSecondary = Color.Black,
    tertiary = CustomBlue, // Optional, if you need a third accent color
    onTertiary = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black,
    secondaryContainer = CustomYellow.copy(alpha = 0.2f), // Dark background for secondary container
    onSecondaryContainer = CustomBlue, // Text color on secondary container
)


// Theme function with a utility for TopAppBar default color
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WexchangerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,

) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val topBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = colorScheme.secondary,
        titleContentColor = colorScheme.primary,
        actionIconContentColor = colorScheme.primary
    )
    val navigationBarColors = NavigationBarItemDefaults.colors(
        selectedIconColor = colorScheme.primary,
        unselectedIconColor = colorScheme.primary
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            CompositionLocalProvider(
                LocalTopAppBarColors provides topBarColors,
                LocalNavigationBarItemColors provides navigationBarColors,
                content = content
            )


        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
val LocalTopAppBarColors = compositionLocalOf<TopAppBarColors> {
    error("No TopAppBarColors provided")
}

@OptIn(ExperimentalMaterial3Api::class)
val LocalNavigationBarItemColors = compositionLocalOf<NavigationBarItemColors> {
    error("No NavigationBarColors provided")
}