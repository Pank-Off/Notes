package ru.kotlincourses.notes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kotlincourses.notes.data.NotesRepository
import ru.kotlincourses.notes.model.Note

class NoteViewModel(private val notesRepository: NotesRepository, var note: Note?) : ViewModel() {
    private val showErrorLiveData = MutableLiveData<Event<Boolean>>()

    private val deleteBtnLiveData = MutableLiveData<DeleteBtnVisibility>()

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
        viewModelScope.launch {
            val noteValue = note ?: return@launch
            try {
                notesRepository.addOrReplaceNote(noteValue)
            } catch (exc: Exception) {
                showErrorLiveData.value = Event(true)
            }
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            val noteValue = note ?: return@launch
            try {
                notesRepository.deleteNote(noteValue.id)
            } catch (exc: Exception) {
                showErrorLiveData.value = Event(true)
            }
        }
    }

    fun setVisibleDeleteBtn(visibility: DeleteBtnVisibility) {
        deleteBtnLiveData.postValue(visibility)
    }

    fun observeDeleteBtnVisible(): LiveData<DeleteBtnVisibility> = deleteBtnLiveData

    fun showError(): LiveData<Event<Boolean>> = showErrorLiveData
}