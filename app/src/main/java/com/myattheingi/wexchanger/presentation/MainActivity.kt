package com.myattheingi.wexchanger.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.myattheingi.wexchanger.presentation.currency_converter.CurrencyConverterScreen
import com.myattheingi.wexchanger.presentation.theme.WexchangerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WexchangerTheme {
                CurrencyConverterScreen(viewModel)
            }
        }
    }
}

