package com.nikitayasiulevich.notesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nikitayasiulevich.notesapp.utils.Constants.Keys.NOTES_TABLE

@Entity(tableName = NOTES_TABLE)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo
    val title: String = "",
    @ColumnInfo
    val subtitle: String = "",
    val firebaseId:String = ""
)
