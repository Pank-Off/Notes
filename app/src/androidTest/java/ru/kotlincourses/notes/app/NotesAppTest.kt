package ru.kotlincourses.notes.app

import android.app.Application
import org.koin.core.context.startKoin

class NotesAppTest : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
        }
    }
}