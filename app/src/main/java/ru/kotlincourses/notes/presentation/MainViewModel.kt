package ru.kotlincourses.notes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ru.kotlincourses.notes.data.NotesRepository

class MainViewModel(private val notesRepository: NotesRepository) : ViewModel() {
    fun observeViewState(): LiveData<NotesViewState> = notesRepository.observeNotes()
        .map {
            if (it.isEmpty()) NotesViewState.EMPTY else NotesViewState.Value(it)
        }
}