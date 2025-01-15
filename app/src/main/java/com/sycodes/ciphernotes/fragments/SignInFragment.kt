package com.sycodes.ciphernotes.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.sycodes.ciphernotes.NotesActivity
import com.sycodes.ciphernotes.R
import com.sycodes.ciphernotes.databinding.FragmentSignInBinding
import com.sycodes.ciphernotes.utility.GoogleSignIn


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var authentication : FirebaseAuth
    private lateinit var GoogleSignIn : GoogleSignIn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authentication = FirebaseAuth.getInstance()
        GoogleSignIn = GoogleSignIn(this) { success, user ->
            if (success) {
                hideProgressBar()
                Toast.makeText(requireContext(), "Signed in as: ${user?.email}", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), NotesActivity::class.java))
                requireActivity().finish()
            } else {
                hideProgressBar()
                showWarning("Sign in failed.")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.goToSignUpFragment.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.loginSignupfragmentContainerView, SignUpFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
        binding.forgotPassword.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.loginSignupfragmentContainerView, ForgotPasswordFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.signInButton.setOnClickListener {
            showProgressBar()
            hideKeyboard()
            userSignIn()
        }

        binding.googleSignInButton.setOnClickListener {
            GoogleSignIn.signIn()
        }

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        GoogleSignIn.handleSignInResult(requestCode, resultCode, data)
    }

    private fun navigateToFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.loginSignupfragmentContainerView, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun userSignIn() {
        val email = binding.signInEmail.text.toString().trim()
        val password = binding.signInPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            showWarning("Please enter both email and password.")
            return
        }

        showProgressBar()

        authentication.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                hideProgressBar()

                if (task.isSuccessful) {
                    val user = authentication.currentUser
                    if (user?.isEmailVerified == true) {
                        startActivity(Intent(requireContext(), NotesActivity::class.java))
                        requireActivity().finish()
                    } else {
                        navigateToFragment(EmailVerifyFragment())
                    }
                } else {
                    handleSignInError(task.exception)
                }
            }
            .addOnFailureListener { exception ->
              hideProgressBar()
                showWarning("Sign in failed: ${exception.localizedMessage}")
            }
    }

    private fun handleSignInError(exception: Exception?) {
        when (exception) {
            is FirebaseAuthInvalidUserException -> {
                if (exception.errorCode == "ERROR_USER_NOT_FOUND") {
                    showWarning("No account found with this email.")
                } else {
                    showWarning("Invalid user.")
                }
            }
            is FirebaseAuthInvalidCredentialsException -> {
                if (exception.errorCode == "ERROR_INVALID_EMAIL") {
                    showWarning("The email address is badly formatted.")
                } else {
                    showWarning("Invalid email or password.")
                }
            }
            else -> {
                showWarning("Sign in failed: ${exception?.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    private fun showWarning(message: String) {
        binding.signInWarningView.apply {
            visibility = View.VISIBLE
            text = message
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
        binding.signInButton.isEnabled = false
    }
    private fun hideProgressBar(){
        binding.progressBar.visibility = View.GONE
        binding.signInButton.isEnabled = true
    }
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(requireContext())
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}