package ru.kotlincourses.notes.presentation

import androidx.lifecycle.ViewModel
import ru.kotlincourses.notes.data.Note
import ru.kotlincourses.notes.data.Repository

class NoteViewModel(var note: Note?) : ViewModel() {

    fun updateNote(text: String) {
        note = (note ?: Note()).copy(note = text)
    }

    fun updateTitle(text: String) {
        note = (note ?: Note()).copy(title = text)
    }

    override fun onCleared() {
        super.onCleared()
        note?.let {
            Repository.addOrReplaceNote(it)
        }
    }
}