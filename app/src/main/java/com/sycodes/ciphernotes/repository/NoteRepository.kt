package com.sycodes.ciphernotes.repository

import androidx.lifecycle.LiveData
import com.sycodes.ciphernotes.data.Note
import com.sycodes.ciphernotes.room.NoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class NoteRepository(private val noteDao: NoteDao, private val firestoreRepository: FirestoreRepository) {

    val allNotes : LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun addNote(note : Note){
        noteDao.insertNote(note)
        withContext(Dispatchers.IO) {
            firestoreRepository.addNote(note)
        }
    }

    suspend fun deleteNote(note : Note) {
        noteDao.deleteNote(note)
        withContext(Dispatchers.IO) {
            firestoreRepository.deleteNote(note)
        }
    }

    suspend fun syncNotesFromFirebase() {
        val firestoreNotes =
            firestoreRepository.getAllNotes().get().await().toObjects(Note::class.java)
        firestoreNotes.forEach { note ->
            noteDao.insertNote(note)
        }
    }
}