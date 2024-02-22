package com.xinhui.mobfinalproject.ui.screens.addUpdateFood.addFood.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.xinhui.mobfinalproject.core.service.StorageService
import com.xinhui.mobfinalproject.core.utils.AlarmManagerHelper
import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.data.repo.product.ProductRepo
import com.xinhui.mobfinalproject.ui.screens.addUpdateFood.baseAddUpdate.viewModel.BaseAddUpdateViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModelImpl @Inject constructor(
    private val productRepo: ProductRepo,
    private val storageService: StorageService
): BaseAddUpdateViewModelImpl(), AddFoodViewModel {

    override fun addProduct(product: Product, uri: Uri?, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val err = productValidate(product)
            if (err == null) {
                _isLoading.emit(true)
                errorHandler {
                    val id = productRepo.addNewProduct(product)
                    uri?.let {
                        storageService.addImage("$id.jpg", it).let { url ->
                            productRepo.updateProduct(product.copy(id = id, productUrl = url))
                        } }
                    AlarmManagerHelper.setAlarms(context, id, product.expiryDate)
                    _isLoading.emit(false)
                    _success.emit("Product added successfully")
                }
            } else _error.emit(err)
        }
    }

}
