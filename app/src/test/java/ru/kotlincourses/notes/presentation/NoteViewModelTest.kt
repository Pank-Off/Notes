package ru.kotlincourses.notes.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.mockk.*
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
    fun setUp() {
        every { notesRepository.addOrReplaceNote(any()) } returns MutableLiveData(
            Result.success(
                Note()
            )
        )
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
    fun `LiveData contains error after save`() {
        every { notesRepository.addOrReplaceNote(any()) } returns MutableLiveData(
            Result.failure(
                IllegalAccessError()
            )
        )
        val currentNote = Note(title = "Hello!")
        noteViewModel = NoteViewModel(notesRepository, currentNote)
        noteViewModel.saveNote()

        Assert.assertTrue(noteViewModel.showError().value != null)
    }

    @Test
    fun `LiveData contains success after delete`() {
        every { notesRepository.deleteNote(any()) } returns MutableLiveData(
            Result.success(
                Note()
            )
        )
        val currentNote = Note(title = "Hello!")
        noteViewModel = NoteViewModel(notesRepository, currentNote)
        noteViewModel.deleteNote()
        Assert.assertTrue(noteViewModel.showError().value == null)
    }

    @Test
    fun `LiveData contains error after delete`() {
        every { notesRepository.deleteNote(any()) } returns MutableLiveData(
            Result.failure(
                IllegalAccessError()
            )
        )
        val currentNote = Note(title = "Hello!")
        noteViewModel = NoteViewModel(notesRepository, currentNote)
        noteViewModel.deleteNote()
        Assert.assertTrue(noteViewModel.showError().value != null)
    }

    @Test
    fun `ViewModel Note title changed`() {
        val currentNote = Note(title = "Hello!")
        noteViewModel = NoteViewModel(notesRepository, currentNote)
        noteViewModel.updateTitle("Alloha")
        Assert.assertEquals("Alloha", noteViewModel.note?.title)
    }

    @Test
    fun `add or replace note called with correct title note`() {
        noteViewModel.updateTitle("Hello")
        noteViewModel.saveNote()
        val slot = slot<Note>()
        verify(exactly = 1) { notesRepository.addOrReplaceNote(capture(slot)) }
        //  verify(exactly = 1) { notesRepository.addOrReplaceNote(match { it.title == "Hello" }) } можно так
        Assert.assertEquals("Hello", slot.captured.title)
    }

    @Test
    fun `should remove observer`() {
        noteViewModel.onCleared()
        Assert.assertFalse(resultLiveData.hasObservers())
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}