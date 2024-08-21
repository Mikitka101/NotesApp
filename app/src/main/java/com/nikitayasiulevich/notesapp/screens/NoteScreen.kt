package com.nikitayasiulevich.notesapp.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.nikitayasiulevich.notesapp.utils.DB_TYPE
import com.nikitayasiulevich.notesapp.utils.TYPE_FIREBASE
import com.nikitayasiulevich.notesapp.utils.TYPE_ROOM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(navController: NavHostController, viewModel: MainViewModel, noteId: String?) {

    val notes = viewModel.readAllNotes().observeAsState(listOf()).value
    val note = when (DB_TYPE.value) {
        TYPE_FIREBASE -> notes.firstOrNull { it.firebaseId == noteId } ?: Note()
        TYPE_ROOM -> notes.firstOrNull { it.id == noteId?.toInt() } ?: Note()
        else -> Note()
    }
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var title by remember {
        mutableStateOf(Constants.Keys.EMPTY)
    }
    var subtitle by remember {
        mutableStateOf(Constants.Keys.EMPTY)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = note.title
                    )
                    Text(
                        text = note.subtitle
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    title = note.title
                    subtitle = note.subtitle
                    showBottomSheet = true
                }) {
                    Text(text = Constants.Keys.EDIT_NOTE)
                }
                Button(onClick = {
                    viewModel.deleteNote(note) {
                        navController.navigate(NavRoute.Main.route)
                    }
                }) {
                    Text(text = Constants.Keys.DELETE_NOTE)
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                tonalElevation = 5.dp,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                onDismissRequest = {
                    showBottomSheet = false
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Text(
                        text = Constants.Keys.EDIT_NOTE,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = {
                            Text(text = Constants.Keys.TITLE)
                        },
                        isError = title.isEmpty()
                    )
                    OutlinedTextField(
                        value = subtitle,
                        onValueChange = { subtitle = it },
                        label = {
                            Text(text = Constants.Keys.SUBTITLE)
                        },
                        isError = subtitle.isEmpty()
                    )
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    showBottomSheet = false
                                }
                                viewModel.updateNote(
                                    Note(
                                        id = note.id,
                                        title = title,
                                        subtitle = subtitle,
                                        firebaseId = note.firebaseId
                                    )
                                ) {
                                    navController.navigate(route = NavRoute.Main.route)
                                }
                            }
                        }
                    ) {
                        Text(text = Constants.Keys.UPDATE_NOTE)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoteScreen() {
    val context = LocalContext.current
    val viewModel: MainViewModel =
        viewModel(factory = MainViewModelFactory(application = context.applicationContext as Application))

    NotesAppTheme {
        NoteScreen(
            navController = rememberNavController(),
            viewModel = viewModel,
            noteId = "1"
        )
    }
}