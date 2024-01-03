package com.xinhui.mobfinalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.GoogleAuthProvider
import com.xinhui.mobfinalproject.core.service.AuthService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 2000
    }

    @Inject
    lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("debugging", "onCreate:${authService.getCurrUser()?.uid}")

        findViewById<MaterialButton>(R.id.mbGoogle).setOnClickListener {
            signIn()
        }
        findViewById<MaterialButton>(R.id.mbSignout).setOnClickListener {
            signOut()
        }
    }


    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode != RESULT_CANCELED) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        lifecycleScope.launch {
            authService.signInWithGoogle(credential)?.let {
                setupGoogleClient(this@MainActivity)
                Log.d("debugging", "firebaseAuthWithGoogle: ${it.uid}")
            }
        }
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    fun setupGoogleClient(activity: AppCompatActivity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ContextCompat.getString(activity, R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun signOut() {
        Log.d("debugging", "onDestroy: somehting")
        mGoogleSignInClient.signOut()
        authService.logout()
        Log.d("debugging", "signOut: ${authService.getCurrUser()}")
    }
}