package ru.kotlincourses.notes

class Model(private val listener: Listener) {

    fun calculateValue() {
        val result = Math.random()
        listener.handle(result.toString())
    }
}