package ru.kotlincourses.notes.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import ru.kotlincourses.notes.data.NotesRepository
import ru.kotlincourses.notes.model.User

class SplashViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val mockRepository: NotesRepository = mockk()
    private val viewStateLiveData = MutableLiveData<SplashViewState>()
    private lateinit var viewModel: SplashViewModel

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel = SplashViewModel(mockRepository)
    }

    @Test
    fun `request user test`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            val user = mockRepository.getCurrentUser()

            //assertEquals and assertSame - different?
            Assert.assertTrue(user == null)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
        clearAllMocks()
    }
}