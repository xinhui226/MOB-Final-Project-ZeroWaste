package com.xinhui.mobfinalproject.ui.screens.login.viewModel

import kotlinx.coroutines.flow.SharedFlow

interface LoginViewModel {
    val loggedIn: SharedFlow<Unit>
    val emailNotVerified: SharedFlow<Unit>
    fun login(
        email: String,
        pass: String
    )

    fun sendResetPasswordLink(email: String)
}
