package ru.kotlincourses.notes.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.main_fragment.*
import ru.kotlincourses.notes.presentation.NotesViewModel
import ru.kotlincourses.notes.R
import ru.kotlincourses.notes.presentation.NotesViewState
import ru.kotlincourses.notes.ui.adapter.NotesAdapter

class MainFragment : Fragment(R.layout.main_fragment) {
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(
            NotesViewModel::class.java
        )
    }
    private val adapter = NotesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainRecycler.adapter = adapter

        viewModel.observeViewState().observe(viewLifecycleOwner) {
            when (it) {
                is NotesViewState.Value -> adapter.submitList(it.notes)
                NotesViewState.EMPTY -> Unit
            }
        }
    }
}


