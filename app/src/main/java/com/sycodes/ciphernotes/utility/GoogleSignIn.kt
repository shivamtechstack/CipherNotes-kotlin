package com.sycodes.ciphernotes.utility

import android.app.Activity
import android.content.Intent
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sycodes.ciphernotes.NotesActivity
import com.sycodes.ciphernotes.R

class GoogleSignIn() {

}