package com.xinhui.mobfinalproject.ui.screens.login.viewModel

import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.data.model.User
import com.xinhui.mobfinalproject.data.repo.user.UserRepo
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class  LoginViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
): BaseViewModel(), LoginViewModel  {

    private val _user = MutableSharedFlow<User>()
    val user: SharedFlow<User> = _user

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
}