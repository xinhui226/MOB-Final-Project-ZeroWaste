package com.xinhui.mobfinalproject.ui.screens.addFood.viewModel

import android.net.Uri
import com.xinhui.mobfinalproject.data.model.Category

interface AddFoodViewModel {
    fun addProduct(
        productName: String,
        storagePlace: String,
        quantity: Int,
        unit: String,
        expiryDate: String,
        productUrl: Uri?,
        category: Category
    )
}