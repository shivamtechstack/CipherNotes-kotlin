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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesWritingBinding.inflate(inflater, container, false)

        binding.saveButton.setOnClickListener {
            saveNote()
        }
        return binding.root
    }

    private fun saveNote(){
        val title = binding.NoteTitle.text
        val content = binding.NoteContent.text
        val currentDateAndTime= CurrentDateAndTime().getCurrentDateTime()
        val note = Note(id = UUID.randomUUID().toString(), title=title.toString(), content=content.toString(), dateCreated = currentDateAndTime, lastModified = currentDateAndTime)

        noteViewModel.addNote(note)

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}