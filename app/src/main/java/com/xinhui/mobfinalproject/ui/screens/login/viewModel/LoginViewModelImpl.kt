package com.xinhui.mobfinalproject.ui.screens.login.viewModel

import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class  LoginViewModelImpl @Inject constructor(

): BaseViewModel(), LoginViewModel  {

    override fun login(email: String, pass: String) {
    }
}