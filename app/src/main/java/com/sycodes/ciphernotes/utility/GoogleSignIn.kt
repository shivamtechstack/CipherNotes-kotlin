package com.sycodes.ciphernotes.utility

import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sycodes.ciphernotes.R
import com.google.android.gms.auth.api.signin.GoogleSignIn

class GoogleSignIn(private val fragment: Fragment,
                   private val onSignInResult: (Boolean, FirebaseUser?) -> Unit)
{
    private val googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(fragment.getString(R.string.Client_ID))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(fragment.requireContext(), gso)
    }

    fun signIn() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun handleSignInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                onSignInResult(false, null)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSignInResult(true, auth.currentUser)
                } else {
                    onSignInResult(false, null)
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 100
    }
}