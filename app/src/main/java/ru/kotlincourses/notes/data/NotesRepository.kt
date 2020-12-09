package ru.kotlincourses.notes.data

import kotlinx.coroutines.flow.Flow
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.model.User

interface NotesRepository {
    suspend fun getCurrentUser(): User?
    fun observeNotes(): Flow<List<Note>>
    suspend fun addOrReplaceNote(newNote: Note)
    suspend fun deleteNote(noteId: Long)
}