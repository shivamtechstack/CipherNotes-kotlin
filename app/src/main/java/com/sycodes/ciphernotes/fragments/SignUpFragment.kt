package com.sycodes.ciphernotes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.sycodes.ciphernotes.R
import com.sycodes.ciphernotes.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var authentication : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authentication = FirebaseAuth.getInstance()

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

        return binding.root
    }
    private fun createUser(email: String, password: String) {
        authentication.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Send a verification email
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
        if (exception is FirebaseAuthUserCollisionException) {
            // Email already in use
            authentication.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    if (it.user?.isEmailVerified == false) {
                        // Resend verification email and update password
                        it.user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                            if (emailTask.isSuccessful) {
                                authentication.currentUser?.updatePassword(password)
                                    ?.addOnCompleteListener { passwordTask ->
                                        if (passwordTask.isSuccessful) {
                                            showWarning("Verification email resent. Check your inbox.")
                                            navigateToEmailVerifyFragment()
                                        } else {
                                            showWarning("Error updating password: ${passwordTask.exception?.localizedMessage}")
                                        }
                                    }
                            } else {
                                showWarning("Error resending verification email: ${emailTask.exception?.localizedMessage}")
                            }
                        }
                    } else {
                        showWarning("Email is already registered and verified.")
                    }
                }
                .addOnFailureListener {
                    showWarning("Error signing in: ${it.localizedMessage}")
                }
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}