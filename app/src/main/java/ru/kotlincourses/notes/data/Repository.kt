package ru.kotlincourses.notes.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.kotlincourses.notes.data.db.DatabaseProvider
import ru.kotlincourses.notes.model.Note
import kotlin.random.Random

private val idRandom = Random(0)
val noteId: Long
    get() = idRandom.nextLong()

class Repository(private val provider: DatabaseProvider) : NotesRepository {

    override fun observeNotes(): Flow<List<Note>> = provider.observeNotes()

    override suspend fun addOrReplaceNote(newNote: Note) =
        withContext(Dispatchers.IO) { provider.addOrReplaceNote(newNote) }

    override suspend fun deleteNote(noteId: Long) =
        withContext(Dispatchers.IO) { provider.deleteNote(noteId) }

    override suspend fun getCurrentUser() =
        withContext(Dispatchers.IO) { provider.getCurrentUser() }
}
