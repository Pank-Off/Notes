package ru.kotlincourses.notes.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.kotlincourses.notes.data.errors.NoAuthException
import ru.kotlincourses.notes.model.Note

class FireStoreDatabaseProviderTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockCollection = mockk<CollectionReference>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()
    private val testNotes = listOf(Note(id = 1), Note(id = 2), Note(id = 3))
    private val provider: FireStoreDatabaseProvider = FireStoreDatabaseProvider(mockDb, mockAuth)

    @Before
    fun setUp() {
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns ""
        every {
            mockDb.collection(any()).document(any()).collection(any())
        } returns mockCollection
        every { mockDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(Note::class.java) } returns testNotes[2]
    }

    @Test(expected = NoAuthException::class)
    fun `should throw if no auth`() {
        every { mockAuth.currentUser } returns null
        provider.observeNotes()
        provider.getCurrentUser() ?: throw NoAuthException()
    }

    @After
    fun tearDown() {
        clearMocks(mockCollection, mockDocument1, mockDocument2, mockDocument3)
    }
}