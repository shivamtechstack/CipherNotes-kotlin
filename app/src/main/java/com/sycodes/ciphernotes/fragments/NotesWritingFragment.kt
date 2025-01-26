package com.sycodes.ciphernotes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sycodes.ciphernotes.data.Note
import com.sycodes.ciphernotes.databinding.FragmentNotesWritingBinding
import com.sycodes.ciphernotes.utility.CurrentDateAndTime
import com.sycodes.ciphernotes.viewmodel.NoteViewModel
import java.util.UUID

class NotesWritingFragment : Fragment() {
    private lateinit var binding: FragmentNotesWritingBinding
    private lateinit var noteViewModel: NoteViewModel
    private var dateAndTime: String? = null

    private var title: String? = null
    private var content: String? = null
    private var dateCreated: String? = null
    private var lastModified: String? = null
    private var isPinned: Boolean = false
    private var isFavourite: Boolean = false

    private var existingNoteId: String? = null
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        dateAndTime = CurrentDateAndTime().getCurrentDateTime()

        arguments?.let {
            existingNoteId = it.getString("noteId")
            title = it.getString("noteTitle")
            content = it.getString("noteContent")
            dateCreated = it.getString("noteDateCreated")
            lastModified = it.getString("noteLastModified")
            isPinned = it.getBoolean("noteIsPinned")
            isFavourite = it.getBoolean("noteIsFavourite")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesWritingBinding.inflate(inflater, container, false)
        binding.dateAndTimeView.text = dateAndTime

        if (existingNoteId != null) {
            isEditMode = true
            binding.dateAndTimeView.text = dateCreated
            binding.NoteTitle.setText(title)
            binding.NoteContent.setText(content)
        }

        binding.saveButton.setOnClickListener {
            saveOrUpdateNote()
        }
        return binding.root
    }

    private fun saveOrUpdateNote() {
        val title = binding.NoteTitle.text.toString()
        val content = binding.NoteContent.text.toString()
        val currentDateAndTime = CurrentDateAndTime().getCurrentDateTime()

        if (isEditMode) {

           val updatedNote = Note(
                    id = existingNoteId!!,
                    title = title,
                    content = content,
                    lastModified = currentDateAndTime,
               isPinned = isPinned,
               isFavourite = isFavourite,
                    dateCreated = dateCreated!!,
               isSynced = false
                )
            noteViewModel.addOrUpdateNote(updatedNote)
        } else {
                val newNote = Note(
                id = UUID.randomUUID().toString(),
                title = title,
                content = content,
                dateCreated = currentDateAndTime,
                lastModified = currentDateAndTime,
                    isPinned = false,
                    isFavourite = false,
            )
            noteViewModel.addOrUpdateNote(newNote)

            isEditMode = true
            existingNoteId = newNote.id
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}