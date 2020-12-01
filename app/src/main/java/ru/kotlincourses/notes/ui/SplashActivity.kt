package ru.kotlincourses.notes.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.kotlincourses.notes.data.notesRepository
import ru.kotlincourses.notes.databinding.ActivitySplashBinding
import ru.kotlincourses.notes.presentation.SplashViewModel
import ru.kotlincourses.notes.presentation.SplashViewState

class SplashActivity : BaseActivity() {
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SplashViewModel(notesRepository) as T
            }
        }).get(
            SplashViewModel::class.java
        )
    }

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        viewModel.observeViewState().observe(this) {
            when (it) {
                is SplashViewState.Error -> renderError(it.error)
                SplashViewState.Auth -> renderData()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode != RC_SIGN_IN -> return
            resultCode != RESULT_OK -> finish()
            resultCode == RESULT_OK -> renderData()
        }
    }
}