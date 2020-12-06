package ru.kotlincourses.notes.model

import android.content.Context
import android.os.Parcelable
import androidx.core.content.ContextCompat
import kotlinx.android.parcel.Parcelize
import ru.kotlincourses.notes.R
import ru.kotlincourses.notes.data.noteId

@Parcelize
data class Note(
    var id: Long = noteId,
    val title: String = "",
    val note: String = "",
    val color: Color = Color.values().toList().shuffled().first(),
) : Parcelable

enum class Color {
    WHITE,
    YELLOW,
    GREEN,
    BLUE,
    RED,
    VIOLET,
    PINK
}

fun Color.mapToColor(context: Context): Int {

    val id = when (this) {
        Color.WHITE -> R.color.color_white
        Color.YELLOW -> R.color.color_yellow
        Color.GREEN -> R.color.color_green
        Color.BLUE -> R.color.color_blue
        Color.RED -> R.color.color_red
        Color.VIOLET -> R.color.color_violet
        Color.PINK -> R.color.color_pink
    }

    return ContextCompat.getColor(context, id)
}