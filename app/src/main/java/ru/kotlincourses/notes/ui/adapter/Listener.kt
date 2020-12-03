package ru.kotlincourses.notes.ui.adapter

import ru.kotlincourses.notes.model.Note

fun interface Listener {
    fun handle(note: Note)
}