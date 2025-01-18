package com.sycodes.ciphernotes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import com.sycodes.ciphernotes.R
import com.sycodes.ciphernotes.databinding.FragmentNotesHomeBinding


class NotesHomeFragment : Fragment() {

    private lateinit var binding: FragmentNotesHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        // Handle Navigation Item Selection
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

            }
            binding.drawerlayout.closeDrawers()
            true
        }
    }
}