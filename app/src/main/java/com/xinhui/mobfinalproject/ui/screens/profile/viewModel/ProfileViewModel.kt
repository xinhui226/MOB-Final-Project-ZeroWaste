package com.xinhui.mobfinalproject.ui.screens.profile.viewModel

import android.net.Uri
import com.xinhui.mobfinalproject.data.model.User
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ProfileViewModel {
    val user: StateFlow<User>
    val profilePic: StateFlow<Uri?>
    val loggedOut: SharedFlow<Unit>

    fun getCurrUser()
    fun getProfileUri()
    fun updateProfileUri(uri: Uri)
    fun logout()
}