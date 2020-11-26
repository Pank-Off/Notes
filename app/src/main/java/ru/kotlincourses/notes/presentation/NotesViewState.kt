package ru.kotlincourses.notes.presentation

import ru.kotlincourses.notes.data.Note

sealed class NotesViewState {
    data class Value(val notes: List<Note>) : NotesViewState()
    object EMPTY : NotesViewState()
}