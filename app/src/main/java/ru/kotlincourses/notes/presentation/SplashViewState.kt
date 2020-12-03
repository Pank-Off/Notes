package ru.kotlincourses.notes.presentation

sealed class SplashViewState {
    class Error(val error: Throwable) : SplashViewState()
    object Auth : SplashViewState()
}