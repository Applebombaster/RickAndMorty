package ru.urfu.rickandmorty.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.urfu.rickandmorty.ui.detail.CharacterDetailScreen
import ru.urfu.rickandmorty.ui.favorites.FavoritesScreen
import ru.urfu.rickandmorty.ui.list.CharacterListScreen
import ru.urfu.rickandmorty.ui.settings.SettingsScreen

private val TOP_LEVEL_ROUTES = setOf(
    Routes.CHARACTERS,
    Routes.FAVORITES,
    Routes.SETTINGS
)

@Composable
fun RickAndMortyApp() {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()
    val route = currentRoute?.destination?.route

    val showBottomBar = route in TOP_LEVEL_ROUTES

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.CHARACTERS,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.CHARACTERS) {
                CharacterListScreen(onCharacterClick = { id ->
                    navController.navigate(Routes.characterDetail(id))
                })
            }

            composable(Routes.FAVORITES) {
                FavoritesScreen()
            }

            composable(Routes.SETTINGS) {
                SettingsScreen()
            }

            composable(
                route = Routes.CHARACTER_DETAIL,
                arguments = listOf(
                    navArgument(Routes.ARG_CHARACTER_ID) { type = NavType.IntType }
                )
            ) {
                CharacterDetailScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}