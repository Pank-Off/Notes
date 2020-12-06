package ru.kotlincourses.notes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kotlincourses.notes.data.NotesRepository
import ru.kotlincourses.notes.data.errors.NoAuthException
import ru.kotlincourses.notes.model.User

class SplashViewModel(private val repository: NotesRepository) : ViewModel() {
    private val viewStateLiveData = MutableLiveData<SplashViewState>()

    init {
        viewModelScope.launch {
            requestUser()
        }
    }

    fun observeViewState(): LiveData<SplashViewState> = viewStateLiveData

    private suspend fun requestUser() {
        var user: User? = null
        withContext(Dispatchers.IO) {
            user = repository.getCurrentUser()
        }
        viewStateLiveData.value =
            user?.let { SplashViewState.Auth } ?: SplashViewState.Error(error = NoAuthException())
    }
}