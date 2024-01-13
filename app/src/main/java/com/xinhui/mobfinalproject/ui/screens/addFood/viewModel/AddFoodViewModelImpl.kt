package com.xinhui.mobfinalproject.ui.screens.addFood.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.xinhui.mobfinalproject.core.service.StorageService
import com.xinhui.mobfinalproject.core.utils.AlarmManagerHelper
import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.data.repo.product.ProductRepo
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModelImpl @Inject constructor(
    private val productRepo: ProductRepo,
    private val storageService: StorageService
): BaseViewModel(), AddFoodViewModel {

    override fun addProduct(product: Product, uri: Uri?, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val err = addProductValidate(product)
            if (err == null) {
                errorHandler {
                    val id = productRepo.addNewProduct(product)
                    uri?.let {
                        storageService.addImage("$id.jpg", it).let { url ->
                            productRepo.updateProduct(product.copy(id = id, productUrl = url))
                        } }
                    AlarmManagerHelper.setAlarms(context, id, product.expiryDate)
                    _success.emit("Product added successfully")
                }
            } else _error.emit(err)
        }
    }

    private fun addProductValidate(product: Product): String?{
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
