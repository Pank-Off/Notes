package ru.kotlincourses.notes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kotlincourses.notes.data.NotesRepository
import ru.kotlincourses.notes.data.Repository
import ru.kotlincourses.notes.data.db.DatabaseProvider
import ru.kotlincourses.notes.data.db.FireStoreDatabaseProvider
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.presentation.MainViewModel
import ru.kotlincourses.notes.presentation.NoteViewModel
import ru.kotlincourses.notes.presentation.SplashViewModel
import kotlin.math.sin

object DependencyGraph {

    private val repositoryModule by lazy {
        module {
            single { Repository(get()) } bind NotesRepository::class
            single { FireStoreDatabaseProvider(get(), get()) } bind DatabaseProvider::class
        }
    }

    private val fireStoreDatabaseProviderModule by lazy {
        module {
            single { FirebaseFirestore.getInstance() }
            single { FirebaseAuth.getInstance() }

        }
    }

    private val viewModelModule by lazy {
        module {
            viewModel { (note: Note?) ->
                NoteViewModel(get(), note)
            }
            viewModel {
                MainViewModel(get())
            }
            viewModel {
                SplashViewModel(get())
            }
        }
    }

    val modules = listOf(repositoryModule, viewModelModule, fireStoreDatabaseProviderModule)
}