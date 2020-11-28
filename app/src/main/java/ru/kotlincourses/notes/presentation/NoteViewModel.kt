package ru.kotlincourses.notes.presentation

import androidx.lifecycle.*
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.data.notesRepository

class NoteViewModel(var note: Note?) : ViewModel(), LifecycleOwner {
    private val showErrorLiveData = MutableLiveData<Event<Boolean>>()

    private val lifecycleOwner: LifecycleOwner = LifecycleOwner { lifecycleRegistry }
    private val lifecycleRegistry = LifecycleRegistry(lifecycleOwner).also {
        it.currentState = Lifecycle.State.RESUMED
    }

    fun updateNote(text: String) {
        note = (note ?: Note()).copy(note = text)
    }

    fun updateTitle(text: String) {
        note = (note ?: Note()).copy(title = text)
    }

    fun saveNote() {
        note?.let { note ->
            notesRepository.addOrReplaceNote(note).observe(lifecycleOwner) {
                it.onFailure {
                    showErrorLiveData.value = Event(true)
                }
            }
        }
    }

    fun showError(): LiveData<Event<Boolean>> = showErrorLiveData


    override fun onCleared() {
        super.onCleared()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}