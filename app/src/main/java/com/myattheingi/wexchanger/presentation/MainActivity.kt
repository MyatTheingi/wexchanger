package com.myattheingi.wexchanger.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myattheingi.wexchanger.R
import com.myattheingi.wexchanger.presentation.components.BottomNavigationBar
import com.myattheingi.wexchanger.presentation.exchange.ExchangeScreen
import com.myattheingi.wexchanger.presentation.home.HomeScreen
import com.myattheingi.wexchanger.presentation.theme.LocalTopAppBarColors
import com.myattheingi.wexchanger.presentation.theme.WexchangerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WexchangerTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(stringResource(id = R.string.title_tool_bar))
                            }, colors = LocalTopAppBarColors.current
                        )
                    },
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = TopLevelRoute.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(TopLevelRoute.Home.route) {
                            HomeScreen()
                        }
                        composable(TopLevelRoute.Exchange.route) {
                            ExchangeScreen()
                        }
                    }
                }
            }
        }
    }
}
