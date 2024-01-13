package com.xinhui.mobfinalproject.ui.screens.addFood.viewModel

import android.net.Uri
import com.xinhui.mobfinalproject.data.model.Product

interface AddFoodViewModel {
    fun addProduct(product: Product, uri: Uri?)
}