package ru.kotlincourses.notes.data.db

import kotlinx.coroutines.flow.Flow
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.model.User

interface DatabaseProvider {
    fun observeNotes(): Flow<List<Note>>
    suspend fun addOrReplaceNote(newNote: Note)
    fun getCurrentUser(): User?
    suspend fun deleteNote(noteId: Long)
}