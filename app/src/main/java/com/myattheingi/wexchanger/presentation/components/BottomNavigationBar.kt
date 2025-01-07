package com.myattheingi.wexchanger.presentation.components


import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.myattheingi.wexchanger.presentation.theme.LocalNavigationBarItemColors
import com.myattheingi.wexchanger.presentation.topLevelRoutes

@Composable
fun BottomNavigationBar(navController: NavHostController, modifier: Modifier = Modifier) {
    NavigationBar(
        modifier = modifier
            .height(56.dp)
            .padding(vertical = 0.dp) // Adjust padding as needed
    ) {
        // Observe the current back stack entry
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        // Iterate through the routes
        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                colors = LocalNavigationBarItemColors.current,
                icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.name) },
                selected = currentDestination?.hierarchy?.any { it.route == topLevelRoute.route } == true,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    BottomNavigationBar(
        navController = rememberNavController()
    )
}