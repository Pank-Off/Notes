package ru.kotlincourses.notes.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.kotlincourses.notes.data.NotesRepository
import ru.kotlincourses.notes.model.Note

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: NotesRepository = mockk()
    private val noteLiveData = MutableLiveData<List<Note>>()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        every { mockRepository.observeNotes() } returns noteLiveData
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `observe notes test`() {
        viewModel.observeViewState()
        verify(exactly = 1) { mockRepository.observeNotes() }
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}