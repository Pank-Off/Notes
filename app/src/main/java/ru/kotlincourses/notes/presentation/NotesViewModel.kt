package ru.kotlincourses.notes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.kotlincourses.notes.data.Repository

class NotesViewModel : ViewModel() {
    private val viewStateLiveData = MutableLiveData<NotesViewState>(NotesViewState.EMPTY)

    init {
        viewStateLiveData.value = NotesViewState.Value(Repository.notes)
    }

    fun observeViewState(): LiveData<NotesViewState> = viewStateLiveData
}