package ru.kotlincourses.notes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.kotlincourses.notes.databinding.MainFragmentBinding
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.presentation.MainViewModel
import ru.kotlincourses.notes.presentation.NotesViewState
import ru.kotlincourses.notes.ui.adapter.NotesAdapter

class MainFragment : Fragment() {
    private val viewModel by viewModel<MainViewModel>()
    private val adapter = NotesAdapter()

    private var _binding: MainFragmentBinding? = null
    private val binding: MainFragmentBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        adapter.attachListener {
            navigateTo(it)
        }
        with(binding) {
            mainRecycler.adapter = adapter
            fab.setOnClickListener {
                navigateTo(null)
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


