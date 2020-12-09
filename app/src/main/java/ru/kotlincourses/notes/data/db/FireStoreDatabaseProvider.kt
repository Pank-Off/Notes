package ru.kotlincourses.notes.data.db

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.kotlincourses.notes.data.errors.NoAuthException
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.model.User
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val TAG = "FireStoreDatabase"
private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreDatabaseProvider(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : DatabaseProvider {
    private val currentUser
        get() = auth.currentUser
    private val result = MutableStateFlow<List<Note>?>(null)
    private var subscribeOnDb = false

    override fun observeNotes(): Flow<List<Note>> {
        if (!subscribeOnDb) subscribeForDbChanging()
        return result.filterNotNull()
    }

    override suspend fun addOrReplaceNote(newNote: Note) {
        suspendCoroutine<Note> { continuation ->
            handleNotesReference(
                {
                    getUserNotesCollection().document(newNote.id.toString())
                        .set(newNote)
                        .addOnSuccessListener {
                            Log.d(TAG, "Note $newNote is saved")
                            continuation.resumeWith(Result.success(newNote))
                        }.addOnFailureListener {
                            Log.e(TAG, "Error saving note $newNote, message: ${it.message}")
                            continuation.resumeWithException(it)
                        }
                }, {
                    Log.e(TAG, "Error saving note $newNote, message: ${it.message}")
                    continuation.resumeWithException(it)
                })
        }
    }

    private fun subscribeForDbChanging() {
        if (subscribeOnDb) return
        handleNotesReference(
            {
                getUserNotesCollection()
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.e(TAG, "Observe note exception:$e")
                        } else if (snapshot != null) {
                            val notes = mutableListOf<Note>()

                            for (doc: QueryDocumentSnapshot in snapshot) {
                                notes.add(doc.toObject(Note::class.java))
                            }
                            result.value = notes
                        }
                    }
                subscribeOnDb = true
            },
            {
                Log.e(TAG, "Error subscribe, message: ${it.message}")
            })
    }

    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override fun getCurrentUser() = currentUser?.run { User(displayName, email) }

    override suspend fun deleteNote(noteId: Long) {
        suspendCancellableCoroutine<Note?> { continuation ->
            handleNotesReference({
                getUserNotesCollection().document(noteId.toString()).delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "Note is deleted")
                        continuation.resumeWith(Result.success(null))
                    }.addOnFailureListener {
                        Log.e(TAG, "Error delete note, message: ${it.message}")
                        continuation.resumeWithException(it)
                    }
            })
        }


    }

    private inline fun handleNotesReference(
        referenceHandler: (CollectionReference) -> Unit,
        exceptionHandler: (Throwable) -> Unit = {}
    ) {
        runCatching {
            getUserNotesCollection()
        }.fold(referenceHandler, exceptionHandler)
    }

}
