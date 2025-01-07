package com.myattheingi.wexchanger.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TopLevelRoute(
    val route: String,
    val name: String,
    val icon: ImageVector
) {
    object Home : TopLevelRoute("home", "Home", Icons.Filled.AccountBalance)
    object Exchange : TopLevelRoute("exchange", "Exchange", Icons.Filled.CurrencyExchange)
}

val topLevelRoutes = listOf(TopLevelRoute.Home, TopLevelRoute.Exchange)