package ru.kotlincourses.notes.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.*
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.model.User

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

    @Test
    fun `should throw if no auth`() {
        //как проверить что вылетел конкретный эксепшн?
        every { mockAuth.currentUser } returns null
        provider.observeNotes()
        val user: User? = provider.getCurrentUser()
        Assert.assertTrue(user == null)
    }

    @Test
    fun `observe notes return notes`() {
        val result = MutableLiveData<List<Note>>()
        val slot = slot<EventListener<QuerySnapshot>>()
        val mockSnapshot = mockk<QuerySnapshot>()

        every { mockSnapshot.documents } returns
                listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.observeNotes().observeForever {
            result.value = it
        }

        slot.captured.onEvent(mockSnapshot, null)
        Assert.assertEquals(testNotes, result.value)
    }

    @After
    fun tearDown() {
        clearMocks(mockCollection, mockDocument1, mockDocument2, mockDocument3)
    }
}