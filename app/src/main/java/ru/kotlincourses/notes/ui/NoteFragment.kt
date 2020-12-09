package ru.kotlincourses.notes.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_note.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.kotlincourses.notes.R
import ru.kotlincourses.notes.databinding.FragmentNoteBinding
import ru.kotlincourses.notes.model.Note
import ru.kotlincourses.notes.presentation.NoteViewModel

class NoteFragment : Fragment() {
    private val note: Note? by lazy(LazyThreadSafetyMode.NONE) { arguments?.getParcelable(EXTRA_NOTE) }
    private val viewModel by viewModel<NoteViewModel> {
        parametersOf(note)
    }
    private var _binding: FragmentNoteBinding? = null
    private val binding: FragmentNoteBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        if (note != null) {
            viewModel.setVisibleDeleteBtn(NoteViewModel.DeleteBtnVisibility.VISIBLE)
        } else {
            viewModel.setVisibleDeleteBtn(NoteViewModel.DeleteBtnVisibility.INVISIBLE)
        }
        with(binding) {
            viewModel.note?.let {
                titleEt.setText(it.title)
                bodyEt.setText(it.note)
            }
            viewModel.showError().observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(context, "Error while saving note!", Toast.LENGTH_LONG).show()
                }
            }
            viewModel.observeDeleteBtnVisible().observe(viewLifecycleOwner) {
                when (it) {
                    NoteViewModel.DeleteBtnVisibility.VISIBLE -> deleteBtn.visibility =
                        Button.VISIBLE
                    NoteViewModel.DeleteBtnVisibility.INVISIBLE -> deleteBtn.visibility =
                        Button.INVISIBLE
                    else -> deleteBtn.visibility = Button.INVISIBLE
                }
            }
            saveBtn.setOnClickListener {
                viewModel.saveNote()
                hideKeyboard()
                activity?.supportFragmentManager?.popBackStack()
            }

            deleteBtn.setOnClickListener {
                AlertDialog.Builder(ContextThemeWrapper(context, R.style.myDialog))
                    .setTitle(R.string.confirm_the_deletion)
                    .setMessage(R.string.sure_delete)
                    .setPositiveButton(R.string.ok_bth_title) { _, _ ->
                        viewModel.deleteNote()
                        parentFragmentManager.popBackStack()
                    }
                    .setNegativeButton(R.string.logout_dialog_cancel) { _, _ -> }
                    .create()
                    .show()
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
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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