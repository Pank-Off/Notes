package ru.kotlincourses.notes.app

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.kotlincourses.notes.di.DependencyGraph

class NotesApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NotesApp)
            modules(DependencyGraph.modules)
        }
    }
}