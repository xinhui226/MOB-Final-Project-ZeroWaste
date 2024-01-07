package com.xinhui.mobfinalproject.data.repo.user

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): UserRepo {

    private fun getDbRef(): CollectionReference {
        return db.collection("users")
    }

    private fun getUid(): String {
        val firebaseUser = authService.getCurrUser()
        return firebaseUser?.uid ?: throw Exception("No user found")
    }

    override suspend fun getUser(): User? {
        val snapshot = getDbRef().document(getUid()).get().await()
        return snapshot.data?.let {
            it["id"] = snapshot.id
            User.fromHashMap(it)
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        getDbRef()
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("debugging:user", "${document.id} => ${document.data}")
                }
            }
        return null
    }

    override suspend fun addNewUser(user: User) {
        getDbRef().document(getUid()).set(user.toHash()).await()
    }

    override suspend fun updateUserDetail(user: User) {
        user.id.let {
            if (it.isNullOrEmpty()) throw Exception("User id not found")
            else getDbRef().document(it).set(user).await()
        }
    }

    override suspend fun deleteUser(id: String) {
        getDbRef().document(id).delete().await()
    }

}