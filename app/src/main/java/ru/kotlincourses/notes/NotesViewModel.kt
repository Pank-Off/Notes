package ru.kotlincourses.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotesViewModel : ViewModel() {
    private val model = Model {
        data.value = it
    }

    private val data = MutableLiveData<String>()

    fun getData(): LiveData<String> = data

    fun buttonClicked() {
        model.calculateValue()
    }
}