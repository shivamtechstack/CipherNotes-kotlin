package com.sycodes.ciphernotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val dateCreated: String = "",
    val lastModified: String = "",
    val isSynced: Boolean = false,
    val idDeleted: Boolean = false,
    val isPinned: Boolean = false,
    val isFavourite: Boolean = false
)
