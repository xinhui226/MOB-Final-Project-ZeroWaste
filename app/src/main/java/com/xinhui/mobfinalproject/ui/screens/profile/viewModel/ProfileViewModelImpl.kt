package com.xinhui.mobfinalproject.ui.screens.profile.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.core.service.StorageService
import com.xinhui.mobfinalproject.data.model.Notification
import com.xinhui.mobfinalproject.data.model.User
import com.xinhui.mobfinalproject.data.repo.notification.NotificationRepo
import com.xinhui.mobfinalproject.data.repo.user.UserRepo
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
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
    private val authService: AuthService,
    private val notificationRepo: NotificationRepo
): BaseViewModel(), ProfileViewModel {

    lateinit var job: Job
    lateinit var scope : CoroutineScope

    private val _user = MutableStateFlow(User(name = "anonymous", email = "anonymous"))
    override val user: StateFlow<User> = _user

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    override val notifications: StateFlow<List<Notification>> = _notifications

    private val _loggedOut = MutableSharedFlow<Unit>()
    override val loggedOut: SharedFlow<Unit> = _loggedOut

    override fun onCreateView() {
        super.onCreateView()
        job = SupervisorJob()
        scope = CoroutineScope(Dispatchers.IO + job)
        getNotification()
    }

    override fun getNotification() {
        scope.launch(Dispatchers.IO) {
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
                _isLoading.emit(true)
                errorHandler {
                    storageService.addImage("${it}.jpg", uri).let { url ->
                        userRepo.updateUserDetail(_user.value.copy(profileUrl = url))
                        getCurrUser()
                        _isLoading.emit(false)
                    }
                }
            }
        }
    }

    override fun updateUsername(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (name.length < 3)
                _error.emit("Name has to be at least 3 characters")
            else {
                _isLoading.emit(true)
                errorHandler {
                    userRepo.updateUserDetail(_user.value.copy(name = name))
                    _isLoading.emit(false)
                }
                getCurrUser()
            }
        }
    }

    override fun logout() {
        job.cancel()
        viewModelScope.launch { _loggedOut.emit(Unit) }
        authService.logout()
    }

    fun delete(notification: Notification) {
        viewModelScope.launch(Dispatchers.IO) {
            notification.id?.let {
                errorHandler{ notificationRepo.deleteNotification(it) }
            }
        }
    }
}