package com.nikitayasiulevich.notesapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.nikitayasiulevich.notesapp.navigation.NavRoute
import com.nikitayasiulevich.notesapp.navigation.NotesNavHost
import com.nikitayasiulevich.notesapp.ui.theme.NotesAppTheme
import com.nikitayasiulevich.notesapp.utils.DB_TYPE

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppTheme {
                val context = LocalContext.current
                val viewModel: MainViewModel =
                    viewModel(factory = MainViewModelFactory(application = context.applicationContext as Application))
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Notes App")
                            },
                            actions = {
                                if(DB_TYPE.value.isNotEmpty()){
                                    Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .clickable {
                                                viewModel.signOut {
                                                    navController.navigate(route = NavRoute.Start.route) {
                                                        popUpTo(NavRoute.Start.route) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                            }
                                    )
                                }

                            },
                            colors = TopAppBarDefaults.topAppBarColors()
                                .copy(
                                    containerColor = Color.Blue,
                                    titleContentColor = Color.White
                                )
                        )
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NotesNavHost(viewModel, navController)
                    }
                }
            }
        }
    }
}
