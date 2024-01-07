package com.xinhui.mobfinalproject.core.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthServiceImpl(
    val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): AuthService {
    override suspend fun signUp(email: String, password: String): FirebaseUser? {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user
    }

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user
    }

    override fun getCurrUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun logout() {
        auth.signOut()
    }

    override fun getUid(): String {
        return auth.currentUser?.uid ?: throw Exception("No authentic user found")
    }

    override suspend fun resetPassword(email: String, onFinish: (msg: String, err: String?) -> Unit) {
        db.collection("users").whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener {
                        if (it.isSuccessful) onFinish("Please check email $email", null)
                        else onFinish("","Something wrong... please try again")
                    }
                } else onFinish("","Account not found, please try with another email")
            }
            .addOnFailureListener { exception ->
                Log.w("debugging", "Error getting documents: ", exception)
                onFinish("","Something wrong... please try again")
            }
    }
}