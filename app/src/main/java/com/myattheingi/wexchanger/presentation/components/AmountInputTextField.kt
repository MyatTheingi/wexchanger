package com.myattheingi.wexchanger.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myattheingi.wexchanger.R
import com.myattheingi.wexchanger.core.utils.toNullableDouble

@Composable
fun AmountInputTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit
) {

    val validAmountMessage = stringResource(id = R.string.error_valid_amount)
    val amountGreaterThanZeroMessage = stringResource(id = R.string.error_amount_greater_than_zero)

    var errorMessage by remember { mutableStateOf("") }


    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = { newValue ->

                val parsedValue = newValue.toNullableDouble()
                if (parsedValue != null || newValue.lastOrNull() != '.') {
                    // Update state with the new value
                    onValueChanged(newValue)
                }

                errorMessage = when {
                    parsedValue == null || parsedValue < 0 -> validAmountMessage
                    parsedValue == 0.0 -> amountGreaterThanZeroMessage
                    else -> ""
                }
            },
            label = { Text(stringResource(R.string.title_amount)) },
            isError = errorMessage.isNotEmpty(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChanged("") }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                    }
                }
            }
        )
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAmountInputTextField() {
    AmountInputTextField(
        value = "1.0",
        onValueChanged = { }
    )
}
