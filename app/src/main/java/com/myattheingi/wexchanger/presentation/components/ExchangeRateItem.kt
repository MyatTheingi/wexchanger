package com.myattheingi.wexchanger.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.myattheingi.wexchanger.R
import com.myattheingi.wexchanger.core.domain.models.CurrencyExchange
import com.myattheingi.wexchanger.core.utils.toFormattedString


@Composable
fun ExchangeRateItem(exchangeRate: CurrencyExchange, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.gap_16), dimensionResource(id = R.dimen.gap_8))
    ) {
        ExchangeRateText(
            text = exchangeRate.code,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleSmall
        )

        ExchangeRateText(
            text = exchangeRate.amountExchangeRate.toFormattedString(),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(2f)
        )
    }
}

