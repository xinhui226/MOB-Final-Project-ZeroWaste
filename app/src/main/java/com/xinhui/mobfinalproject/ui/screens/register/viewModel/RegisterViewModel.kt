package com.xinhui.mobfinalproject.ui.screens.register.viewModel

interface RegisterViewModel {
    fun register(
        name: String,
        email: String,
        pass: String,
        confirmPass: String
    )
}