package com.myattheingi.wexchanger.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.myattheingi.wexchanger.R

@Composable
fun ExchangeRateHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(dimensionResource(id = R.dimen.gap_16), dimensionResource(id = R.dimen.gap_14))
    ) {
        ExchangeRateText(
            text = R.string.title_currency,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleSmall
        )

        ExchangeRateText(
            text = R.string.title_rate,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.titleSmall
        )
    }
}