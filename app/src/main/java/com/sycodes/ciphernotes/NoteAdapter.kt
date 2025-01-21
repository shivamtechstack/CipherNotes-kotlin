package com.sycodes.ciphernotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sycodes.ciphernotes.data.Note

class NoteAdapter(private var notes: List<Note>, private val listener: (Note) -> Unit) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteTitle: TextView = itemView.findViewById(R.id.AllNotesTitle)
        private val noteContent: TextView = itemView.findViewById(R.id.allNotesContent)
        private val noteTime: TextView = itemView.findViewById(R.id.AllNotesTime)

        fun bind(note: Note, listener: (Note) -> Unit) {
            noteTitle.text = note.title
            noteContent.text = note.content
            noteTime.text = note.lastModified
            itemView.setOnClickListener { listener(note) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.allnoteslayout, parent, false)
        return NoteViewHolder(inflater)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position], listener)
    }

    fun updateNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
