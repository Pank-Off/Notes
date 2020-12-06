package ru.kotlincourses.notes.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.*
import ru.kotlincourses.notes.data.NotesRepository

class SplashViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: NotesRepository = mockk()
    private val viewStateLiveData = MutableLiveData<SplashViewState>()
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        viewModel = SplashViewModel(mockRepository)
        every { viewModel.observeViewState() } returns viewStateLiveData
    }

    @Test
    fun `request user test`() {
        val user = mockRepository.getCurrentUser()
        //assertEquals and assertSame - different?
        Assert.assertTrue(user == null)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}