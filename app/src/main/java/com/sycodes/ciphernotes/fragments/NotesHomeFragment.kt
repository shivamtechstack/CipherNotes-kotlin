package com.sycodes.ciphernotes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.sycodes.ciphernotes.utility.BottomSheetDialogOptions
import com.sycodes.ciphernotes.viewmodel.NoteViewModel

class NotesHomeFragment : Fragment() {

    private lateinit var binding: FragmentNotesHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var bottomSheetDialogOptions: BottomSheetDialogOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        bottomSheetDialogOptions = BottomSheetDialogOptions(requireContext(), layoutInflater)
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
                putString("noteLastModified", note.lastModified)
                putBoolean("noteIsPinned", note.isPinned)
                putBoolean("noteIsFavourite", note.isFavourite)
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
                bottomSheetDialogOptions.bottomPopUpDialog(note, noteViewModel)
            })

        binding.notesRecyclerView.adapter = noteAdapter
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        noteViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.updateNotes(notes)
        }

        return binding.root
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
        
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

            }
            binding.drawerlayout.closeDrawers()
            true
        }
    }
}