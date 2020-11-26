package ru.kotlincourses.notes.ui.adapter

import ru.kotlincourses.notes.data.Note

fun interface Listener {
    fun handle(note: Note)
}