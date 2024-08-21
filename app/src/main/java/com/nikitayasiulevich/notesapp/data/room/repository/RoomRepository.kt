package com.nikitayasiulevich.notesapp.data.room.repository

import androidx.lifecycle.LiveData
import com.nikitayasiulevich.notesapp.data.DatabaseRepository
import com.nikitayasiulevich.notesapp.data.room.dao.NoteRoomDao
import com.nikitayasiulevich.notesapp.model.Note

class RoomRepository(private val noteRoomDao: NoteRoomDao): DatabaseRepository {
    override val readAll: LiveData<List<Note>>
        get() = noteRoomDao.getAllNotes()

    override suspend fun create(note: Note, onSuccess: () -> Unit) {
        noteRoomDao.addNote(note)
        onSuccess.invoke()
    }

    override suspend fun delete(note: Note, onSuccess: () -> Unit) {
        noteRoomDao.deleteNote(note)
        onSuccess.invoke()
    }

    override suspend fun update(note: Note, onSuccess: () -> Unit) {
        noteRoomDao.updateNote(note)
        onSuccess.invoke()
    }

    override fun signOut() { }
}