package ru.kotlincourses.notes.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.kotlincourses.notes.R
import ru.kotlincourses.notes.data.Note
import ru.kotlincourses.notes.data.mapToColor

val DIFF_UTIL: DiffUtil.ItemCallback<Note> = object : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return true
    }
}

class NotesAdapter : ListAdapter<Note, NotesAdapter.NoteViewHolder>(DIFF_UTIL) {

    private lateinit var listener: Listener
    fun attachListener(clickListener: Listener) {
        listener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_note, parent, false
        )
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val clickListener: View.OnClickListener = View.OnClickListener {
            listener.handle(currentNote)
        }
        private lateinit var currentNote: Note
        fun bind(item: Note) {
            currentNote = item
            with(itemView) {
                title.text = item.title
                body.text = item.note
                setBackgroundColor(item.color.mapToColor(context))
                setOnClickListener(clickListener)
            }
        }
    }
}
