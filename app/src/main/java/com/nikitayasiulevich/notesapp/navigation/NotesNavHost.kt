package com.nikitayasiulevich.notesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nikitayasiulevich.notesapp.screens.AddScreen
import com.nikitayasiulevich.notesapp.screens.MainScreen
import com.nikitayasiulevich.notesapp.screens.NoteScreen
import com.nikitayasiulevich.notesapp.screens.StartScreen

sealed class NavRoute(val route: String) {
    data object Start: NavRoute("start_screen")
    data object Main: NavRoute("main_screen")
    data object Add: NavRoute("add_screen")
    data object Note: NavRoute("note_screen")
}

@Composable
fun NotesNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoute.Start.route) {
        composable(NavRoute.Start.route) { StartScreen(navController = navController) }
        composable(NavRoute.Main.route) { MainScreen(navController = navController) }
        composable(NavRoute.Add.route) { AddScreen(navController = navController) }
        composable(NavRoute.Note.route) { NoteScreen(navController = navController) }
    }
}