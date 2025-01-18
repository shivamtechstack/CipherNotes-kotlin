package com.sycodes.ciphernotes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sycodes.ciphernotes.data.Note
import com.sycodes.ciphernotes.repository.FirestoreRepository
import com.sycodes.ciphernotes.repository.NoteRepository
import com.sycodes.ciphernotes.room.NoteDatabase
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application) {

    private val repository : NoteRepository
    private val allNotes : LiveData<List<Note>>

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()

        val firestoreRepository = FirestoreRepository()

        repository = NoteRepository(noteDao, firestoreRepository)

        allNotes = repository.allNotes

        viewModelScope.launch {
            repository.syncNotesFromFirebase()
        }
    }

    fun addNote(note: Note) = viewModelScope.launch {
        repository.addNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }
}