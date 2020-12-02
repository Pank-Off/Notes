package ru.kotlincourses.notes.data.db

import androidx.lifecycle.LiveData
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.model.User

interface DatabaseProvider {
    fun observeNotes(): LiveData<List<Note>>
    fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>>
    fun getCurrentUser(): User?
    fun deleteNote(noteId: Long): LiveData<Result<Note?>>
}