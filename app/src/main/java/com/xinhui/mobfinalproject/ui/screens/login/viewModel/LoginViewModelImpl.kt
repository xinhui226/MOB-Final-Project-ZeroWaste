package com.xinhui.mobfinalproject.ui.screens.login.viewModel

import android.util.Patterns
import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel
class  LoginViewModelImpl @Inject constructor(
    private val authService: AuthService,
): BaseViewModel(), LoginViewModel  {

    override fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = errorHandler {
                authService.signIn(email, pass)
            }

            if(res !=  null) {
                _success.emit("Login Successfully")
            } else {
                _error.emit("Login Failed")
            }
        }
    }

    override fun sendResetPasswordLink(email: String) {
        viewModelScope.launch {
            if (email.isEmpty()) _error.emit("Email can't be empty")
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) _error.emit("Invalid email address")
            else authService.resetPassword(email) { msg, err ->
                runBlocking{
                    if (err != null) _error.emit(err)
                    else _success.emit(msg)
                }
            }
        }
    }
}