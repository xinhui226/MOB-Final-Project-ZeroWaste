package com.xinhui.mobfinalproject.ui.screens.home.viewModel

import android.content.Context
import com.xinhui.mobfinalproject.data.model.Product
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {
    val products: StateFlow<List<Product>>

    fun getProducts()
    fun getProducts(category: String)
    fun deleteProduct(id:String, context: Context)
}