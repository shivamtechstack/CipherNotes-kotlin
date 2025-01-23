package com.sycodes.ciphernotes.repository

import androidx.lifecycle.LiveData
import com.sycodes.ciphernotes.data.Note
import com.sycodes.ciphernotes.room.NoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class NoteRepository(private val noteDao: NoteDao, private val firestoreRepository: FirestoreRepository) {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun addOrUpdateNote(note: Note){
        withContext(Dispatchers.IO){
            noteDao.insertNote(note)
        }
    }

    suspend fun deleteNote(note: Note){
        withContext(Dispatchers.IO){
            noteDao.delete(note.id)
        }
    }

}