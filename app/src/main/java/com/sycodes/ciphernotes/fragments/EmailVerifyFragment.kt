package com.sycodes.ciphernotes.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sycodes.ciphernotes.NotesActivity
import com.sycodes.ciphernotes.R
import com.sycodes.ciphernotes.databinding.FragmentEmailVerifyBinding

class EmailVerifyFragment : Fragment() {
    private var _binding: FragmentEmailVerifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var authentication : FirebaseAuth
    private var verificationCheckHandler: Handler? = null
    private var isVerificationChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authentication = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailVerifyBinding.inflate(inflater, container, false)

        binding.emailTextBox.text = "Verification link sent to ${authentication.currentUser?.email}"

        binding.wrongEmailButton.setOnClickListener {
            fragmentManager?.popBackStack()
        }

       binding.openEmailButton.setOnClickListener {
          openEmailApps()
       }
        binding.requestNewLink.setOnClickListener {
            resendVerificationLink()
        }
        startVerificationCheck()

        return binding.root
    }

    private fun openEmailApps() {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_APP_EMAIL)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            startActivity(Intent.createChooser(intent, "Choose Email App"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No email apps found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resendVerificationLink(){
        val user = authentication.currentUser
        if (user != null && !user.isEmailVerified) {
            user.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Verification email sent again", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to send email: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "No user is signed in or email already verified", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startVerificationCheck() {
        verificationCheckHandler = Handler(Looper.getMainLooper())
        verificationCheckHandler?.post(object : Runnable {
            override fun run() {
                authentication.currentUser?.reload()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val isVerified = authentication.currentUser?.isEmailVerified == true
                        if (isVerified && !isVerificationChecked) {
                            isVerificationChecked = true
                            Toast.makeText(requireContext(), "Email verified successfully!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(requireContext(), NotesActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else if (!isVerified) {
                            // Schedule the next check if not verified yet
                            verificationCheckHandler?.postDelayed(this, 5000)
                        }
                    } else {
                        // Handle reload failure
                        Toast.makeText(
                            requireContext(),
                            "Failed to reload user: ${task.exception?.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        startVerificationCheck()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        verificationCheckHandler?.removeCallbacksAndMessages(null)
        _binding = null
    }
}