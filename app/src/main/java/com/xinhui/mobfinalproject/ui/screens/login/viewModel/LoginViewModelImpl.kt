package com.xinhui.mobfinalproject.ui.screens.login.viewModel

import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class  LoginViewModelImpl @Inject constructor(
    private val authService: AuthService,
): BaseViewModel(), LoginViewModel  {

    private val _loggedIn = MutableSharedFlow<Unit>()
    override val loggedIn: SharedFlow<Unit> = _loggedIn

    override fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = errorHandler {
                authService.signIn(email, pass)
            }

            if(res !=  null) {
                _success.emit("Login Successfully")
                _loggedIn.emit(Unit)
            } else {
                _error.emit("Invalid credential")
            }
        }
    }
}
