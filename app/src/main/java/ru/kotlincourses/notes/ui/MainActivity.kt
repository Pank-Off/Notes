package ru.kotlincourses.notes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.kotlincourses.notes.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.main_fragment, MainFragment())
                .commit()
        }
    }
}