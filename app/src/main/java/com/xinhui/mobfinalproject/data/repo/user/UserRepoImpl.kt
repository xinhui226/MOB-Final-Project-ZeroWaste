package com.xinhui.mobfinalproject.data.repo.user

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

    override suspend fun addNewUser(user: User) {
        getDbRef().document(getUid()).set(user.toHash()).await()
    }

    override suspend fun updateUserDetail(user: User) {
        getDbRef().document().set(user.id.toString()).await()
    }

    override suspend fun deleteUser(id: String) {
        getDbRef().document(id).delete().await()
    }

}