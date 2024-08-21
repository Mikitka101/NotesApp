package com.nikitayasiulevich.notesapp.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.nikitayasiulevich.notesapp.navigation.NavRoute
import com.nikitayasiulevich.notesapp.ui.theme.NotesAppTheme
import com.nikitayasiulevich.notesapp.utils.Constants
import com.nikitayasiulevich.notesapp.utils.DB_TYPE
import com.nikitayasiulevich.notesapp.utils.LOGIN
import com.nikitayasiulevich.notesapp.utils.PASSWORD
import com.nikitayasiulevich.notesapp.utils.TYPE_FIREBASE
import com.nikitayasiulevich.notesapp.utils.TYPE_ROOM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(navController: NavHostController, viewModel: MainViewModel) {

    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var login by remember {
        mutableStateOf(Constants.Keys.EMPTY)
    }
    var password by remember {
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
            Text(
                text = Constants.Keys.WHAT_WILL_WE_USE
            )
            Button(
                onClick = {
                    viewModel.initDatabase(TYPE_ROOM) {
                        DB_TYPE.value = TYPE_ROOM
                        navController.navigate(route = NavRoute.Main.route)
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = Constants.Keys.ROOM_DATABASE
                )
            }
            Button(
                onClick = {
                    showBottomSheet = true
                },
                modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = Constants.Keys.FIREBASE_DATABASE
                )
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
                        text = Constants.Keys.LOG_IN,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    OutlinedTextField(
                        value = login,
                        onValueChange = { login = it },
                        label = {
                            Text(text = Constants.Keys.LOGIN_TEXT)
                        },
                        isError = login.isEmpty()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Text(text = Constants.Keys.PASSWORD_TEXT)
                        },
                        isError = password.isEmpty()
                    )
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            LOGIN = login
                            PASSWORD = password
                            viewModel.initDatabase(TYPE_FIREBASE) {
                                DB_TYPE.value = TYPE_FIREBASE
                                navController.navigate(route = NavRoute.Main.route)
                            }
                        },
                        enabled = password.isNotEmpty() && password.isNotEmpty()
                    ) {
                        Text(text = Constants.Keys.SIGN_IN)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStartScreen() {
    val context = LocalContext.current
    val mViewModel: MainViewModel =
        viewModel(factory = MainViewModelFactory(application = context.applicationContext as Application))

    NotesAppTheme {
        StartScreen(navController = rememberNavController(), viewModel = mViewModel)
    }
}