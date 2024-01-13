package com.xinhui.mobfinalproject.ui.screens.profile.viewModel

import android.net.Uri
import com.xinhui.mobfinalproject.data.model.Notification
import com.xinhui.mobfinalproject.data.model.User
import kotlinx.coroutines.flow.StateFlow

interface ProfileViewModel {
    val user: StateFlow<User>
    val notifications: StateFlow<List<Notification>>

    fun getNotification()
    fun getCurrUser()
    fun updateProfileUri(uri: Uri)
    fun updateUsername(name: String)
    fun logout()
}