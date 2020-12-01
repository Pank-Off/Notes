package ru.kotlincourses.notes.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater

import org.koin.androidx.viewmodel.ext.android.viewModel

import ru.kotlincourses.notes.databinding.ActivitySplashBinding
import ru.kotlincourses.notes.presentation.SplashViewModel
import ru.kotlincourses.notes.presentation.SplashViewState

class SplashActivity : BaseActivity() {

    private val viewModel by viewModel<SplashViewModel>()

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