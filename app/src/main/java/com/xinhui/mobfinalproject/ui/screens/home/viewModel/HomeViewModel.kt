package com.xinhui.mobfinalproject.ui.screens.home.viewModel

import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.data.model.User
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {
    val user: StateFlow<User>
    val products: StateFlow<List<Product>>

    fun getCurrUser()
    fun getProducts()
    fun getProductsByCategory(category: String)
}