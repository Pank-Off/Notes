package ru.kotlincourses.notes.presentation

import androidx.lifecycle.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kotlincourses.notes.data.NotesRepository

class MainViewModel(notesRepository: NotesRepository) : ViewModel() {
    private val mainLiveData = MutableLiveData<NotesViewState>()

    init {
        notesRepository.observeNotes().onEach {
            mainLiveData.value =
                if (it.isEmpty()) NotesViewState.EMPTY else NotesViewState.Value(it)
        }
            .launchIn(viewModelScope)
    }

    fun observeViewState(): LiveData<NotesViewState> = mainLiveData

}