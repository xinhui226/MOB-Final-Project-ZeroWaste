package com.xinhui.mobfinalproject.ui.screens.register.viewModel

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.data.model.User
import com.xinhui.mobfinalproject.data.repo.user.UserRepo
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel(), RegisterViewModel {

    override fun register(name: String, email: String, pass: String, confirmPass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            registrationValidate(name, email, pass, confirmPass).let {
                if (it.isNullOrEmpty()) {
                    val user = errorHandler {
                        authService.signUp(email, pass)
                    }
                    if(user != null) {
                        authService.sendEmailVerification()
                        _success.emit("Register Successfully")
                        errorHandler { userRepo.addNewUser(User(name = name, email = email)) }
                    }
                } else _error.emit(it)
            }
        }
    }

    private fun registrationValidate(name: String, email: String, pass: String, confirmPass: String): String? {
        return if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty())
            "Please fill in all fields"
         else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
             "Invalid email address format"
         else if (pass.length < 6)
             "Password must be at least 6 characters"
        else if (pass != confirmPass)
            "Password and Confirm Password is not the same"
        else
            null
    }

}
