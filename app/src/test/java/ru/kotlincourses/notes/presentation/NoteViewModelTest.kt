package ru.kotlincourses.notes.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.*
import ru.kotlincourses.notes.data.NotesRepository
import ru.kotlincourses.notes.model.Note

class NoteViewModelTest {

    private val notesRepository = mockk<NotesRepository>(relaxed = false)
    private lateinit var noteViewModel: NoteViewModel

    private var _resultLiveData = MutableLiveData(Result.success(Note()))
    private val resultLiveData: LiveData<Result<Note>> get() = _resultLiveData

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule() //если работаешь с liveData надо добавить это

    @Before
    fun setUp() = runBlocking {
        coEvery {
            notesRepository.addOrReplaceNote(any())
        }
        noteViewModel = NoteViewModel(notesRepository, null)
    }

    @Test
    fun `LiveData contains success after save`() {
        val currentNote = Note(title = "Hello!")
        noteViewModel = NoteViewModel(notesRepository, currentNote)
        noteViewModel.saveNote()

        Assert.assertTrue(noteViewModel.showError().value == null)
    }

    @Test
    fun `LiveData contains success after delete`() {
        coEvery { notesRepository.deleteNote(any()) }
        val currentNote = Note(title = "Hello!")
        noteViewModel = NoteViewModel(notesRepository, currentNote)
        noteViewModel.deleteNote()
        Assert.assertTrue(noteViewModel.showError().value == null)
    }

    @Test
    fun `ViewModel Note title changed`() {
        val currentNote = Note(title = "Hello!")
        noteViewModel = NoteViewModel(notesRepository, currentNote)
        noteViewModel.updateTitle("Alloha")
        Assert.assertEquals("Alloha", noteViewModel.note?.title)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}