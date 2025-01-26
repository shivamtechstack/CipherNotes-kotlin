package com.sycodes.ciphernotes.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sycodes.ciphernotes.data.Note

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNotes(notes: List<Note>)

    @Query("DELETE FROM notes_table WHERE id = :noteId")
    suspend fun delete(noteId: String)

    @Query("SELECT * FROM notes_table WHERE id = :noteId")
    suspend fun getNoteById(noteId: String): Note

    @Query("SELECT * FROM notes_table ORDER BY lastModified DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE isSynced = 0")
    suspend fun getUnsyncedNotes(): List<Note>

    @Query("SELECT * FROM notes_table WHERE idDeleted = 1 AND isSynced = 0")
    suspend fun getDeletedNotes(): List<Note>

    @Query("SELECT * FROM notes_table WHERE isPinned = 1")
    fun getPinnedNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE isFavourite = 1")
    fun getFavouriteNotes(): LiveData<List<Note>>

}