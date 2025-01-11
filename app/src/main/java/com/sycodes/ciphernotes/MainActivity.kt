package com.sycodes.ciphernotes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import com.sycodes.ciphernotes.fragments.SignInFragment

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentContainer: FragmentContainerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fragmentContainer = findViewById(R.id.loginSignupfragmentContainerView)

        supportFragmentManager.beginTransaction().replace(R.id.loginSignupfragmentContainerView, SignInFragment()).commit()
    }
}