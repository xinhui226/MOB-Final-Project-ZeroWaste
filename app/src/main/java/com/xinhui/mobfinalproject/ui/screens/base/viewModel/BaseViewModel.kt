package com.xinhui.mobfinalproject.ui.screens.base.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel: ViewModel() {
    protected val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    protected val _success = MutableSharedFlow<String>()
    val success: SharedFlow<String> = _success

    protected val _isLoading: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isLoading: SharedFlow<Boolean> = _isLoading

    open fun onCreateView(){}

    suspend fun <T> errorHandler(callback: suspend () -> T): T? {
        return try {
            callback()
        } catch (e: Exception){
            e.printStackTrace()
            _error.emit(e.message.toString())
            null
        }
    }
}