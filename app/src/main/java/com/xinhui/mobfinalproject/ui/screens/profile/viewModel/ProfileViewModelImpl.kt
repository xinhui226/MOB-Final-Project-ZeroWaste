package com.xinhui.mobfinalproject.ui.screens.profile.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.core.service.StorageService
import com.xinhui.mobfinalproject.data.model.User
import com.xinhui.mobfinalproject.data.repo.user.UserRepo
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModelImpl @Inject constructor(
    private val userRepo: UserRepo,
    private val storageService: StorageService,
    private val authService: AuthService
): BaseViewModel(), ProfileViewModel {

    protected val _user = MutableStateFlow(User(name = "anonymous", email = "anonymous"))
    override val user: StateFlow<User> = _user
    protected val _profilePic = MutableStateFlow<Uri?>(null)
    override val profilePic: StateFlow<Uri?> = _profilePic
    protected val _loggedOut = MutableSharedFlow<Unit>()
    override val loggedOut: SharedFlow<Unit> = _loggedOut

    init {
        getCurrUser()
        getProfileUri()
    }

    override fun getCurrUser() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { userRepo.getUser() }?.let { user ->
                _user.emit(user)
            }
        }
    }

    override fun getProfileUri() {
        viewModelScope.launch(Dispatchers.IO) {
            authService.getCurrUser()?.uid.let { id ->
                Log.d("debugging", "getProfileUri: $id")
                _profilePic.value = storageService.getImage("$id.jpg")
            }
        }
    }

    override fun updateProfileUri(uri: Uri) {
        user.value.id?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val name = "${it}.jpg"
                storageService.addImage(name,uri)
                getProfileUri()
            }
        }
    }

    override fun logout() {
        authService.logout()
        viewModelScope.launch { _loggedOut.emit(Unit) }
    }
}