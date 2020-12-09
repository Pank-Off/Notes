import android.view.View
import android.widget.Button
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import ru.kotlincourses.notes.R
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.presentation.NoteViewModel
import ru.kotlincourses.notes.ui.NoteFragment


@RunWith(AndroidJUnit4::class)
class NoteFragmentTest {

    private val mockViewModel: NoteViewModel = mockk(relaxed = true)

    @Before
    fun setup() {
        kotlin.runCatching { startKoin { } }
        loadKoinModules(
            module {
                viewModel { (_: Note?) ->
                    mockViewModel
                }
            }
        )
    }

    @After
    fun clean() {
        stopKoin()
    }

    @Test
    fun save_note_on_button_click() {
        launchFragmentInContainer<NoteFragment>(themeResId = R.style.Theme_AppCompat)

        onView(withId(R.id.saveBtn)).perform(click())

        verify { mockViewModel.saveNote() }
    }

    @Test
    fun delete_note_on_button_click() {

        launchFragmentInContainer<NoteFragment>(themeResId = R.style.Theme_AppCompat)

        onView(withId(R.id.deleteBtn)).perform(setBtnViewVisibility(true))
        onView(withId(R.id.deleteBtn)).perform(click())
        onView(withId(android.R.id.button1)).perform(click())
        verify { mockViewModel.deleteNote() }
    }

    companion object {
        private fun setBtnViewVisibility(visibility: Boolean): ViewAction? {
            return object : ViewAction {
                override fun getConstraints(): org.hamcrest.Matcher<View>? {
                    return isAssignableFrom(Button::class.java)
                }

                override fun perform(uiController: UiController?, view: View) {
                    view.visibility = if (visibility) View.VISIBLE else View.GONE
                }

                override fun getDescription(): String {
                    return "Show / Hide View"
                }
            }
        }
    }

}