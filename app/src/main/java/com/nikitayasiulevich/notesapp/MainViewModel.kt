package com.nikitayasiulevich.notesapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nikitayasiulevich.notesapp.data.firebase.AppFirebaseRepository
import com.nikitayasiulevich.notesapp.data.room.AppRoomDatabase
import com.nikitayasiulevich.notesapp.data.room.repository.RoomRepository
import com.nikitayasiulevich.notesapp.model.Note
import com.nikitayasiulevich.notesapp.utils.Constants
import com.nikitayasiulevich.notesapp.utils.DB_TYPE
import com.nikitayasiulevich.notesapp.utils.REPOSITORY
import com.nikitayasiulevich.notesapp.utils.TYPE_FIREBASE
import com.nikitayasiulevich.notesapp.utils.TYPE_ROOM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val context = application
    fun initDatabase(type: String, onSuccess: () -> Unit) {
        Log.d("MyLog", "MainViewModel.initDatabase() with type: $type")
        when (type) {
            TYPE_ROOM -> {
                val dao = AppRoomDatabase.getInstance(context = context).getRoomDao()
                REPOSITORY = RoomRepository(dao)
                onSuccess()
            }

            TYPE_FIREBASE -> {
                REPOSITORY = AppFirebaseRepository()
                REPOSITORY.connectToDatabase(
                    {
                        onSuccess()
                    },
                    {
                        Log.d("MyLog", "Error: $it")
                    }
                )
            }
        }
    }

    fun addNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.create(note = note) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun updateNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.update(note = note) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun deleteNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.delete(note = note) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun readAllNotes() = REPOSITORY.readAll

    fun signOut(onSuccess: () -> Unit) {
        when (DB_TYPE.value) {
            TYPE_FIREBASE,
            TYPE_ROOM -> {
                REPOSITORY.signOut()
                DB_TYPE.value = Constants.Keys.EMPTY
                onSuccess()
            }

            else -> {
                Log.d("MyLog", "Failed to sign out ${DB_TYPE.value}")
            }
        }
    }
}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}