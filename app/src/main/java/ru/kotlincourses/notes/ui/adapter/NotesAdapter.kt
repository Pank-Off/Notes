package ru.kotlincourses.notes.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kotlincourses.notes.databinding.ItemNoteBinding
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.model.mapToColor

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
        return NoteViewHolder(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteViewHolder(
        parent: ViewGroup, private val binding: ItemNoteBinding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        private val clickListener: View.OnClickListener = View.OnClickListener {
            listener.handle(currentNote)
        }
        private lateinit var currentNote: Note
        fun bind(item: Note) {
            currentNote = item
            with(binding) {
                title.text = item.title
                body.text = item.note
                root.setBackgroundColor(item.color.mapToColor(root.context))
                root.setOnClickListener(clickListener)
            }
        }
    }
}
