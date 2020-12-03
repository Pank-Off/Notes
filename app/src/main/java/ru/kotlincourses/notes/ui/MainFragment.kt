package ru.kotlincourses.notes.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.main_fragment.*
import ru.kotlincourses.notes.R
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.presentation.MainViewModel
import ru.kotlincourses.notes.presentation.NotesViewState
import ru.kotlincourses.notes.ui.adapter.NotesAdapter

class MainFragment : Fragment(R.layout.main_fragment) {
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(
            MainViewModel::class.java
        )
    }
    private val adapter = NotesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        adapter.attachListener {
            navigateTo(it)
        }
        mainRecycler.adapter = adapter

        fab.setOnClickListener {
            navigateTo(null)
        }
        viewModel.observeViewState().observe(viewLifecycleOwner) {
            when (it) {
                is NotesViewState.Value -> adapter.submitList(it.notes)
                NotesViewState.EMPTY -> Unit
            }
        }
    }

    private fun navigateTo(note: Note?) {
        (requireActivity() as MainActivity).navigateTo(NoteFragment.create(note))
    }
}


