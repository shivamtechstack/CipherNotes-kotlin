package com.sycodes.ciphernotes.utility

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sycodes.ciphernotes.R
import com.sycodes.ciphernotes.data.Note
import com.sycodes.ciphernotes.viewmodel.NoteViewModel

class BottomSheetDialogOptions(private val context: Context,private val layoutInflater: LayoutInflater) {
    fun bottomPopUpDialog(note: Note, noteViewModel: NoteViewModel) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val bottomSheetView = layoutInflater.inflate(R.layout.note_options_bottom_sheet, null)

        val noteTitleTextView = bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_note_title)
        val noteDateCreatedTextView = bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_note_createdDate)
        val noteModifiedDateTextView = bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_note_modifiedDate)

        val deleteNoteButton = bottomSheetView.findViewById<FloatingActionButton>(R.id.bottom_sheet_note_DeleteButton)
        val pinNoteButton = bottomSheetView.findViewById<FloatingActionButton>(R.id.bottom_sheet_note_PinButton)
        val favouriteNoteButton = bottomSheetView.findViewById<FloatingActionButton>(R.id.bottom_sheet_note_FavouriteButton)

        pinNoteButton.setImageResource(if (note.isPinned) R.drawable.thumbtack_24_filled else R.drawable.thumbtack_24)
        favouriteNoteButton.setImageResource(if (note.isFavourite) R.drawable.star_24_filled else R.drawable.star_24)

        noteTitleTextView.text = note.title
        noteDateCreatedTextView.text = note.dateCreated
        noteModifiedDateTextView.text = note.lastModified

        deleteNoteButton.setOnClickListener {
            noteViewModel.deleteNote(note)
            bottomSheetDialog.dismiss()
        }

        pinNoteButton.setOnClickListener {
            val updatedNote = note.copy(
                isPinned = !note.isPinned,
                lastModified = CurrentDateAndTime().getCurrentDateTime()
            )
            noteViewModel.addOrUpdateNote(updatedNote)
            pinNoteButton.setImageResource(if (note.isPinned) R.drawable.thumbtack_24_filled else R.drawable.thumbtack_24)
        }

        favouriteNoteButton.setOnClickListener {
            val updatedNote = note.copy(
                isFavourite = !note.isFavourite,
                lastModified = CurrentDateAndTime().getCurrentDateTime()
            )
            noteViewModel.addOrUpdateNote(updatedNote)
            favouriteNoteButton.setImageResource(if (note.isFavourite) R.drawable.star_24_filled else R.drawable.star_24)
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
}