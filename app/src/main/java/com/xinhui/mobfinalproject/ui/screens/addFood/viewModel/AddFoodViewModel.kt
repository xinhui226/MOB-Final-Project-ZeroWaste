package com.xinhui.mobfinalproject.ui.screens.addFood.viewModel

import com.xinhui.mobfinalproject.data.model.Category

interface AddFoodViewModel {
    fun addProduct(
        productName: String,
        storagePlace: String,
        quantity: Int,
        unit: String,
        expiryDate: String,
        productUrl: String,
        category: Category
    )
}