package com.nikitayasiulevich.notesapp.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
import com.nikitayasiulevich.notesapp.utils.DB_TYPE
import com.nikitayasiulevich.notesapp.utils.TYPE_FIREBASE
import com.nikitayasiulevich.notesapp.utils.TYPE_ROOM

@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel) {

    val notes = viewModel.readAllNotes().observeAsState(listOf()).value

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(route = NavRoute.Add.route)
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = Constants.Keys.ADD_ICON
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(notes) { note ->
                NoteItem(note = note, navController = navController)
            }
        }

    }
}

@Composable
private fun NoteItem(
    note: Note,
    navController: NavHostController
) {
    val noteId = when (DB_TYPE.value) {
        TYPE_FIREBASE -> note.firebaseId
        TYPE_ROOM -> note.id
        else -> Constants.Keys.EMPTY
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .clickable {
                navController.navigate(route = NavRoute.Note.route + "/${noteId}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = note.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = note.subtitle
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    val context = LocalContext.current
    val viewModel: MainViewModel =
        viewModel(factory = MainViewModelFactory(application = context.applicationContext as Application))

    NotesAppTheme {
        MainScreen(navController = rememberNavController(), viewModel = viewModel)
    }
}