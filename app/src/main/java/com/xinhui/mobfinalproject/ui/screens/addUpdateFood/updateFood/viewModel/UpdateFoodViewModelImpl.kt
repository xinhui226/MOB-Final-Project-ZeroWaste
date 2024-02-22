package com.xinhui.mobfinalproject.ui.screens.addUpdateFood.updateFood.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.xinhui.mobfinalproject.core.service.StorageService
import com.xinhui.mobfinalproject.core.utils.AlarmManagerHelper
import com.xinhui.mobfinalproject.core.utils.Category
import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.data.repo.product.ProductRepo
import com.xinhui.mobfinalproject.ui.screens.addUpdateFood.baseAddUpdate.viewModel.BaseAddUpdateViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateFoodViewModelImpl @Inject constructor(
    private val productRepo: ProductRepo,
    private val storageService: StorageService,
    stateHandle: SavedStateHandle
) : BaseAddUpdateViewModelImpl(), UpdateFoodViewModel {

    private val id = stateHandle["productId"] ?: "-1"

    private val _product = MutableStateFlow(
        Product(
            quantity = 0,
            productName = "",
            unit = "",
            storagePlace = "",
            expiryDate = "",
            category = Category.others))
    override val product: StateFlow<Product> = _product

    init {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler{ productRepo.getProductById(id)?.let {
                _product.emit(it)
            } }
        }
    }
    override fun updateFood(product: Product, uri: Uri?, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val err = productValidate(product)
            if (err == null) {
                _isLoading.emit(true)
                var updatedProduct = product.copy(id = id, productUrl = _product.value.productUrl)
                errorHandler {
                    uri?.let {
                        storageService.addImage("${id}.jpg", it).let { url ->
                            updatedProduct = updatedProduct.copy(productUrl = url)
                        } }
                    productRepo.updateProduct(updatedProduct)
                    AlarmManagerHelper.cancelAlarms(context, id)
                    AlarmManagerHelper.setAlarms(context, id, product.expiryDate)
                    _isLoading.emit(false)
                    _success.emit("Product updated successfully")
                }
            } else _error.emit(err)
        }
    }
}