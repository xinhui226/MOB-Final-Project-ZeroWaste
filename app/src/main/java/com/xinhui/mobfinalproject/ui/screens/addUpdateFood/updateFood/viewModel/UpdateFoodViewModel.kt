package com.xinhui.mobfinalproject.ui.screens.addUpdateFood.updateFood.viewModel

import android.content.Context
import android.net.Uri
import com.xinhui.mobfinalproject.data.model.Product
import kotlinx.coroutines.flow.StateFlow

interface UpdateFoodViewModel {
    val product: StateFlow<Product>

    fun updateFood(product: Product, uri: Uri?, context: Context)
}