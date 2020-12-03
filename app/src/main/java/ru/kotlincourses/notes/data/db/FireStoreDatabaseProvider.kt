package ru.kotlincourses.notes.data.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import ru.kotlincourses.notes.data.errors.NoAuthException
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.model.User

const val TAG = "FireStoreDatabase"
private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreDatabaseProvider : DatabaseProvider {
    private val db = FirebaseFirestore.getInstance()
    private val result = MutableLiveData<List<Note>>()
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    private var subscribeOnDb = false

    override fun observeNotes(): LiveData<List<Note>> {
        if (!subscribeOnDb) subscribeForDbChanging()
        return result
    }

    override fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>> {
        val result = MutableLiveData<Result<Note>>()

        handleNotesReference(
            {
                getUserNotesCollection().document(newNote.id.toString())
                    .set(newNote).addOnSuccessListener {
                        Log.d(TAG, "Note $newNote is saved")
                        result.value = Result.success(newNote)
                    }.addOnFailureListener {
                        Log.e(TAG, "Error saving note $newNote, message: ${it.message}")
                        result.value = Result.failure(it)
                    }

            }, {
                Log.e(TAG, "Error saving note $newNote, message: ${it.message}")
            })
        return result
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

    private inline fun handleNotesReference(
        referenceHandler: (CollectionReference) -> Unit,
        exceptionHandler: (Throwable) -> Unit = {}
    ) {
        runCatching {
            getUserNotesCollection()
        }.fold(referenceHandler, exceptionHandler)
    }

}
