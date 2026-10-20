package ru.urfu.rickandmorty.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit
)

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(Routes.CHARACTERS, "Персонажи") {
            Icon(Icons.Filled.Person, contentDescription = null)
        },
        BottomNavItem(Routes.FAVORITES, "Избранное") {
            Icon(Icons.Filled.Favorite, contentDescription = null)
        },
        BottomNavItem(Routes.SETTINGS, "Настройки") {
            Icon(Icons.Filled.Settings, contentDescription = null)
        }
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Не копим историю табов: зашли в «Избранное» → нажали «Персонажи» → в бэкстеке одна запись.
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = item.icon,
                label = { Text(item.label) }
            )
        }
    }
}