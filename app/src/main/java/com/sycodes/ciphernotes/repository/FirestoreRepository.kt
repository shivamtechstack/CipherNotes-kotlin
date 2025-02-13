package com.sycodes.ciphernotes.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sycodes.ciphernotes.data.Note
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

    private var userId = FirebaseAuth.getInstance().currentUser?.uid

    private val notesCollection = db.collection("users").document(userId!!).collection("notes")

    suspend fun uploadNoteToFirebase(note: Note) {
        notesCollection.document(note.id).set(note).await()
    }

    suspend fun updateNotesFromFirebase() : List<Note> {
        return notesCollection.get().await().toObjects(Note::class.java)
        }

    suspend fun deleteNoteFromFirebase(note: Note) {
        notesCollection.document(note.id).delete().await()
    }
}