package com.sycodes.ciphernotes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sycodes.ciphernotes.R
import com.sycodes.ciphernotes.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}