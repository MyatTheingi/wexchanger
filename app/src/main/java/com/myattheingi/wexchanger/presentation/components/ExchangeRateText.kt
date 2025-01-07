package com.myattheingi.wexchanger.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ExchangeRateText(
    text: Any,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    style: TextStyle = MaterialTheme.typography.bodySmall
) {
    val textToDisplay = when (text) {
        is Int -> stringResource(id = text)  // Handle String resource ID
        is String -> text  // Handle plain String
        else -> ""  // Fallback to empty string if the type is unexpected
    }

    Text(
        text = textToDisplay,
        textAlign = textAlign,
        modifier = modifier,
        style = style
    )
}
