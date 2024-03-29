package com.xinhui.mobfinalproject.ui.screens.addUpdateFood.addFood.viewModel

import android.content.Context
import android.net.Uri
import com.xinhui.mobfinalproject.data.model.Product

interface AddFoodViewModel {
    fun addProduct(product: Product, uri: Uri?, context: Context)
}