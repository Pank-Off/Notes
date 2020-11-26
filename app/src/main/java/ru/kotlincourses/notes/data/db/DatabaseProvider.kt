package ru.kotlincourses.notes.data.db

import androidx.lifecycle.LiveData
import ru.kotlincourses.notes.data.Note

interface DatabaseProvider {
    fun observeNotes(): LiveData<List<Note>>
    fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>>
}