package com.xinhui.mobfinalproject.core.service

import com.google.firebase.auth.FirebaseUser

interface AuthService {
    suspend fun signUp(email:String, password:String): FirebaseUser?
    suspend fun signIn(email:String, password:String): FirebaseUser?
    fun getCurrUser(): FirebaseUser?
    fun logout()
    fun getUid():String
    fun updatePassword(
        existPassword: String,
        newPassword:String,
        onFinish: (msg: String, err: String?) -> Unit
    )
}