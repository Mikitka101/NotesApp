package com.nikitayasiulevich.notesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nikitayasiulevich.notesapp.MainViewModel
import com.nikitayasiulevich.notesapp.screens.AddScreen
import com.nikitayasiulevich.notesapp.screens.MainScreen
import com.nikitayasiulevich.notesapp.screens.NoteScreen
import com.nikitayasiulevich.notesapp.screens.StartScreen
import com.nikitayasiulevich.notesapp.utils.Constants

sealed class NavRoute(val route: String) {
    data object Start: NavRoute(Constants.Screens.START_SCREEN)
    data object Main: NavRoute(Constants.Screens.MAIN_SCREEN)
    data object Add: NavRoute(Constants.Screens.ADD_SCREEN)
    data object Note: NavRoute(Constants.Screens.NOTE_SCREEN)
}

@Composable
fun NotesNavHost(viewModel: MainViewModel, navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoute.Start.route) {
        composable(NavRoute.Start.route) { StartScreen(navController = navController, viewModel = viewModel) }
        composable(NavRoute.Main.route) { MainScreen(navController = navController, viewModel = viewModel) }
        composable(NavRoute.Add.route) { AddScreen(navController = navController, viewModel = viewModel) }
        composable(NavRoute.Note.route + "/{${Constants.Keys.ID}}") { backStackEntry ->
            NoteScreen(navController = navController, viewModel = viewModel, noteId = backStackEntry.arguments?.getString(Constants.Keys.ID))
        }
    }
}