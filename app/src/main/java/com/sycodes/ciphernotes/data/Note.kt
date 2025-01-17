package com.sycodes.ciphernotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val dateCreated: Int = 0,
    val lastModified: Int = 0,
    val isPinned: Boolean = false,
    val isLocked: Boolean = false,
)
