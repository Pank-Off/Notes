package ru.kotlincourses.notes.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_note.*
import ru.kotlincourses.notes.R
import ru.kotlincourses.notes.data.Note
import ru.kotlincourses.notes.presentation.NoteViewModel

class NoteFragment : Fragment(R.layout.fragment_note) {
    private val note: Note? by lazy(LazyThreadSafetyMode.NONE) { arguments?.getParcelable(EXTRA_NOTE) }
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return NoteViewModel(note) as T
            }
        }).get(
            NoteViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewModel.note?.let {
            titleEt.setText(it.title)
            bodyEt.setText(it.note)
        }
        viewModel.showError().observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context, "Error while saving note!", Toast.LENGTH_LONG).show()
            }
        }
        saveBtn.setOnClickListener {
            viewModel.saveNote()
            activity?.supportFragmentManager?.popBackStack()
        }
        titleEt.addTextChangedListener {
            if (it.toString() != "") {
                viewModel.updateTitle(it.toString())
            }
        }
        bodyEt.addTextChangedListener {
            if (it.toString() != "") {
                viewModel.updateNote(it.toString())
            }
        }
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentManager.popBackStack()
            fragmentTransaction.commit()
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    companion object {
        const val EXTRA_NOTE = "arg"
        fun create(note: Note?): NoteFragment {
            val noteFragment = NoteFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_NOTE, note)
            noteFragment.arguments = bundle
            return noteFragment
        }
    }
}