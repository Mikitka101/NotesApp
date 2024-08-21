package com.nikitayasiulevich.notesapp.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nikitayasiulevich.notesapp.MainViewModel
import com.nikitayasiulevich.notesapp.MainViewModelFactory
import com.nikitayasiulevich.notesapp.model.Note
import com.nikitayasiulevich.notesapp.navigation.NavRoute
import com.nikitayasiulevich.notesapp.ui.theme.NotesAppTheme
import com.nikitayasiulevich.notesapp.utils.Constants

@Composable
fun AddScreen(navController: NavHostController, viewModel: MainViewModel) {

    var title by remember {
        mutableStateOf("")
    }
    var subtitle by remember {
        mutableStateOf("")
    }
    var isButtonEnabled by remember {
        mutableStateOf(false)
    }

    Scaffold(

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = Constants.Keys.ADD_NEW_NOTE,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    isButtonEnabled = title.isNotEmpty() && subtitle.isNotEmpty()
                },
                label = {
                    Text(text = Constants.Keys.NOTE_TITLE)
                },
                isError = title.isEmpty()
            )

            OutlinedTextField(
                value = subtitle,
                onValueChange = {
                    subtitle = it
                    isButtonEnabled = title.isNotEmpty() && subtitle.isNotEmpty()
                },
                label = {
                    Text(text = Constants.Keys.NOTE_SUBTITLE)
                },
                isError = subtitle.isEmpty()
            )

            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    viewModel.addNote(note = Note(title = title, subtitle = subtitle)) {
                        navController.navigate(route = NavRoute.Main.route)
                    }
                },
                enabled = isButtonEnabled
            ) {
                Text(text = Constants.Keys.ADD_NOTE)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddScreen() {
    val context = LocalContext.current
    val viewModel: MainViewModel =
        viewModel(factory = MainViewModelFactory(application = context.applicationContext as Application))

    NotesAppTheme {
        AddScreen(navController = rememberNavController(), viewModel = viewModel)
    }
}