package com.xinhui.mobfinalproject.ui.screens.register.viewModel

import androidx.lifecycle.viewModelScope
import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.data.model.User
import com.xinhui.mobfinalproject.data.repo.user.UserRepo
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel(), RegisterViewModel {

    private val _user = MutableStateFlow(User(name = "Name", email = "Email"))
    val user: StateFlow<User> = _user

    override fun register(name: String, email: String, pass: String, confirmPass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = errorHandler {
                authService.signUp(email, pass)
            }

            if(user != null) {
                _success.emit("Register Successfully")
                errorHandler {
                    userRepo.addNewUser(
                        User(
                            name = name,
                            email = email
                        )
                    )
                }
            }

        }


    }

}