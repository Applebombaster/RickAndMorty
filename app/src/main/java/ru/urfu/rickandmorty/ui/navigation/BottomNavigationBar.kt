package ru.urfu.rickandmorty.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.urfu.rickandmorty.data.local.SettingsStateHolder

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

    // Подписываемся на состояние настроек для бейджа
    val hasActiveSettings by SettingsStateHolder.hasActiveSettings.collectAsState()

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    if (item.route == Routes.SETTINGS && hasActiveSettings) {
                        BadgedBox(badge = { Badge() }) {
                            item.icon()
                        }
                    } else {
                        item.icon()
                    }
                },
                label = { Text(item.label) }
            )
        }
    }
}