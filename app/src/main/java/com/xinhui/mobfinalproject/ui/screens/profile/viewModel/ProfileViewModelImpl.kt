package com.xinhui.mobfinalproject.ui.screens.profile.viewModel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.core.service.StorageService
import com.xinhui.mobfinalproject.data.model.Notification
import com.xinhui.mobfinalproject.data.model.User
import com.xinhui.mobfinalproject.data.repo.notification.NotificationRepo
import com.xinhui.mobfinalproject.data.repo.user.UserRepo
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModelImpl @Inject constructor(
    private val userRepo: UserRepo,
    private val storageService: StorageService,
    private val authService: AuthService,
    private val notificationRepo: NotificationRepo
): BaseViewModel(), ProfileViewModel {

    private val _user = MutableStateFlow(User(name = "anonymous", email = "anonymous"))
    override val user: StateFlow<User> = _user

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    override val notifications: StateFlow<List<Notification>> = _notifications


    init {
        getCurrUser()
        getNotification()
    }

    override fun getNotification() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                notificationRepo.getNotifications().collect {
                    _notifications.emit(it)
                }
            }
        }
    }

    override fun getCurrUser() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { userRepo.getUser() }?.let { user ->
                _user.emit(user)
            }
        }
    }

    override fun updateProfileUri(uri: Uri) {
        user.value.id?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val name = "${it}.jpg"
                storageService.addImage(name,uri).let { url ->
                    userRepo.updateUserDetail(_user.value.copy(profileUrl = url))
                    getCurrUser()
                }
            }
        }
    }

    override fun updateUsername(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (name.length < 3)
                _error.emit("Name has to be at least 3 characters")
            else {
                errorHandler { userRepo.updateUserDetail(_user.value.copy(name = name)) }
                getCurrUser()
            }
        }
    }

    override fun logout() {
        authService.logout()
    }

    fun delete(notification: Notification) {
        viewModelScope.launch(Dispatchers.IO) {
            notification.id?.let { notificationRepo.deleteNotification(it) }
        }
    }
}