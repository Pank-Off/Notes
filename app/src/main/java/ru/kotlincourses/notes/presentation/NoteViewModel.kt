package ru.kotlincourses.notes.presentation

import androidx.lifecycle.*
import ru.kotlincourses.notes.data.NotesRepository
import ru.kotlincourses.notes.model.Note

class NoteViewModel(private val notesRepository: NotesRepository, var note: Note?) : ViewModel(),
    LifecycleOwner {
    private val showErrorLiveData = MutableLiveData<Event<Boolean>>()

    private val deleteBtnLiveData = MutableLiveData<DeleteBtnVisibility>()
    private val lifecycleOwner: LifecycleOwner = LifecycleOwner { lifecycleRegistry }
    private val lifecycleRegistry = LifecycleRegistry(lifecycleOwner).also {
        it.currentState = Lifecycle.State.RESUMED
    }

    enum class DeleteBtnVisibility {
        VISIBLE, INVISIBLE
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

    fun deleteNote() {
        note?.let { note ->
            notesRepository.deleteNote(noteId = note.id).observe(lifecycleOwner) {
                it.onFailure {
                    showErrorLiveData.value = Event(true)
                }
            }
        }
    }

    fun setVisibleDeleteBtn(visibility: DeleteBtnVisibility) {
        deleteBtnLiveData.postValue(visibility)
    }

    fun observeDeleteBtnVisible(): LiveData<DeleteBtnVisibility> = deleteBtnLiveData

    fun showError(): LiveData<Event<Boolean>> = showErrorLiveData

    override fun onCleared() {
        super.onCleared()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}