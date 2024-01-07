package com.xinhui.mobfinalproject.data.repo.user

import com.xinhui.mobfinalproject.data.model.User

interface UserRepo {
    suspend fun getUser(): User?
    suspend fun addNewUser(user: User)
    suspend fun updateUserDetail(user: User)
    suspend fun deleteUser(id:String)
}