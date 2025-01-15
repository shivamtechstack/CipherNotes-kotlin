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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.sycodes.ciphernotes.NotesActivity
import com.sycodes.ciphernotes.R
import com.sycodes.ciphernotes.databinding.FragmentSignUpBinding
import com.sycodes.ciphernotes.utility.GoogleSignIn


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var authentication : FirebaseAuth
    private lateinit var googleSignIn: GoogleSignIn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authentication = FirebaseAuth.getInstance()

        googleSignIn = GoogleSignIn(this) { success, user ->
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
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.backArrowSignUp.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.SignUpButton.setOnClickListener {
            hideKeyboard()
            val email = binding.emailSignUp.text.toString()
            val password = binding.passwordSignUp.text.toString()
            val confirmPassword = binding.confirmPasswordSignUp.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    createUser(email, password)
                }else{
                    showWarning("Passwords do not match")
                }
            }else{
                showWarning("Please fill all the fields")
            }
        }

        binding.googleSignUpButton.setOnClickListener {
            showProgressBar()
            googleSignIn.signIn()
        }

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleSignIn.handleSignInResult(requestCode, resultCode, data)
    }

    private fun createUser(email: String, password: String) {

        showProgressBar()

        authentication.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    binding.progressBarSignUp.visibility = View.GONE
                    binding.SignUpButton.isEnabled = true

                    val user = authentication.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            navigateToEmailVerifyFragment()
                        } else {
                            showWarning("Error sending verification email: ${emailTask.exception?.localizedMessage}")
                        }
                    }
                } else {
                    handleSignUpError(email, password, task.exception)
                }
            }
    }

    private fun handleSignUpError(email: String, password: String, exception: Exception?) {
           hideProgressBar()

            if (exception is FirebaseAuthUserCollisionException) {
                // Email already in use
                showWarning("This email is already registered. Please use the 'Forgot Password' option if you need to reset your password.")
            } else {
                showWarning("Sign-up failed: ${exception?.localizedMessage}")
            }
        }

    private fun navigateToEmailVerifyFragment() {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.loginSignupfragmentContainerView, EmailVerifyFragment())
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun showWarning(message: String) {
        binding.signUpWarningView.visibility = View.VISIBLE
        binding.signUpWarningView.text = message
    }
    private fun showProgressBar(){
        binding.progressBarSignUp.visibility = View.VISIBLE
        binding.SignUpButton.isEnabled = false
    }
    private fun hideProgressBar(){
        binding.progressBarSignUp.visibility = View.GONE
        binding.SignUpButton.isEnabled = true
    }
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(requireContext())
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}