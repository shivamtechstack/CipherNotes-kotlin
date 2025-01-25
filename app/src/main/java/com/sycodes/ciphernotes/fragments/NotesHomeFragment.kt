package com.sycodes.ciphernotes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sycodes.ciphernotes.NoteAdapter
import com.sycodes.ciphernotes.R
import com.sycodes.ciphernotes.data.Note
import com.sycodes.ciphernotes.databinding.FragmentNotesHomeBinding
import com.sycodes.ciphernotes.viewmodel.NoteViewModel

class NotesHomeFragment : Fragment() {

    private lateinit var binding: FragmentNotesHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesHomeBinding.inflate(inflater, container, false)
        drawerSetup()
        binding.AddNoteFloatingActionButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.notesFragmentContainer, NotesWritingFragment())
                addToBackStack(null)
                commit()
            }
        }

        noteAdapter = NoteAdapter(emptyList(),
            { note ->
            val bundle = Bundle().apply {
                putString("noteId", note.id)
                putString("noteTitle", note.title)
                putString("noteContent", note.content)
                putString("noteDateCreated", note.dateCreated)
            }
            val notesWritingFragment = NotesWritingFragment().apply {
                arguments = bundle
            }
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.notesFragmentContainer, notesWritingFragment)
                addToBackStack(null)
                commit()
            }
        },
            { note ->
                popBottomSheet(note)
            })

        binding.notesRecyclerView.adapter = noteAdapter
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        noteViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.updateNotes(notes)
        }

        return binding.root
    }

    private fun popBottomSheet(note: Note) {

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.note_options_bottom_sheet, null)

        // Get references to the views
        val noteTitleTextView = bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_note_title)
        val noteDateCreatedTextView = bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_note_createdDate)
        val noteModifiedDateTextView = bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_note_modifiedDate)
        val deleteNoteButton = bottomSheetView.findViewById<FloatingActionButton>(R.id.bottom_sheet_note_DeleteButton)

        // Set note details
        noteTitleTextView.text = note.title
        noteDateCreatedTextView.text = note.dateCreated

        // Handle delete button click
        deleteNoteButton.setOnClickListener {
            noteViewModel.deleteNote(note) // Ensure your NoteViewModel has a deleteNote() function
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

    }

    private fun drawerSetup(){
        toggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerlayout,
            binding.notesToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle Navigation Item Selection
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

            }
            binding.drawerlayout.closeDrawers()
            true
        }
    }
}