package com.xinhui.mobfinalproject.ui.screens.addUpdateFood.baseAddUpdate.viewModel

import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel

abstract class BaseAddUpdateViewModelImpl : BaseViewModel(), BaseAddUpdate {

    protected fun productValidate(product: Product): String?{
        if (product.quantity == 0) {
            return "Quantity can't be zero"
        }
        else if (
            product.productName.isEmpty() ||
            product.storagePlace.isEmpty() ||
            product.unit.isEmpty() || product.expiryDate.isEmpty()) {
            return "Please fill up all fields"
        }
        return null
    }
}