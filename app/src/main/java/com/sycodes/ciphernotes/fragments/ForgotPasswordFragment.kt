package com.sycodes.ciphernotes.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sycodes.ciphernotes.R
import com.sycodes.ciphernotes.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var authentication : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authentication = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        binding.buttonForgotPassword.setOnClickListener {
            hideKeyboard()
            sendPasswordResetEmail()
        }
        binding.backToLogin.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        return binding.root
    }
    private fun sendPasswordResetEmail() {
        val email = binding.emailForgotPassword.text.toString().trim()

        if (email.isEmpty()) {
            showWarningText("Please enter your email address")
            return
        }

        authentication.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showWarningText("If the email is registered, a password reset link has been sent. Please check your inbox and spam/junk folder.")

            } else {
                // Handle potential Firebase errors (e.g., invalid email format)
                showWarningText(task.exception?.localizedMessage ?: "Failed to send password reset email")
            }
        }.addOnFailureListener {
            // Handle any unexpected failures
            showWarningText(it.localizedMessage ?: "An error occurred")
        }
    }

    private fun showWarningText(message: String) {
        binding.forgotPasswordWarningText.visibility = View.VISIBLE
        binding.forgotPasswordWarningText.text = message
    }
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(requireContext())
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}