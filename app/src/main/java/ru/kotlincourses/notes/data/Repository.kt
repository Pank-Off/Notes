package ru.kotlincourses.notes.data

import androidx.lifecycle.LiveData
import ru.kotlincourses.notes.data.db.DatabaseProvider
import ru.kotlincourses.notes.model.Note
import kotlin.random.Random

private val idRandom = Random(0)
val noteId: Long
    get() = idRandom.nextLong()

class Repository(private val provider: DatabaseProvider) : NotesRepository {

    override fun observeNotes(): LiveData<List<Note>> {
        return provider.observeNotes()
    }

    override fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>> {
        return provider.addOrReplaceNote(newNote)
    }

    override fun getCurrentUser() = provider.getCurrentUser()
}
