package ru.kotlincourses.notes.data

import androidx.lifecycle.LiveData
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.model.User

interface NotesRepository {
    fun getCurrentUser(): User?
    fun observeNotes(): LiveData<List<Note>>
    fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>>
    fun deleteNote(noteId: Long): LiveData<Result<Note?>>
}