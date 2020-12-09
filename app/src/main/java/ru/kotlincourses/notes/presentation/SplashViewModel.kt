package ru.kotlincourses.notes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kotlincourses.notes.data.NotesRepository
import ru.kotlincourses.notes.data.errors.NoAuthException

class SplashViewModel(private val repository: NotesRepository) : ViewModel() {
    private val viewStateLiveData = MutableLiveData<SplashViewState>()

    init {
        viewModelScope.launch {
            requestUser()
        }
    }

    fun observeViewState(): LiveData<SplashViewState> = viewStateLiveData
    private suspend fun requestUser() {
        val user = repository.getCurrentUser()
        viewStateLiveData.value =
            user?.let { SplashViewState.Auth } ?: SplashViewState.Error(error = NoAuthException())
    }
}