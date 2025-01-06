package com.myattheingi.wexchanger.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = CustomBlue,
    secondary = CustomYellow,
    tertiary = CustomBlue,
)

private val LightColorScheme = lightColorScheme(
    primary = CustomBlue,
    secondary = CustomYellow,
    tertiary = CustomBlue,
)

// Theme function with a utility for TopAppBar default color
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WexchangerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            CompositionLocalProvider(
                LocalTopAppBarColors provides topBarColors,
                content = content
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
val LocalTopAppBarColors = compositionLocalOf<TopAppBarColors> {
    error("No TopAppBarColors provided")
}