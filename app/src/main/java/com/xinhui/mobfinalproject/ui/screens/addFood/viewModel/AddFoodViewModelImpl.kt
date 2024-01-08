package com.xinhui.mobfinalproject.ui.screens.addFood.viewModel

import androidx.lifecycle.viewModelScope
import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.core.service.StorageService
import com.xinhui.mobfinalproject.data.model.Category
import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.data.repo.product.ProductRepo
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModelImpl @Inject constructor(
    private val productRepo: ProductRepo,
    private val authService: AuthService,
    private val storageService: StorageService
): BaseViewModel(), AddFoodViewModel {

    override fun addProduct(
        productName: String,
        storagePlace: String,
        quantity: Int,
        unit: String,
        expiryDate: String,
        productUrl: String,
        category: Category
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                productRepo.addNewProduct(
                    Product(
                        productName = productName,
                        quantity = quantity,
                        expiryDate = expiryDate,
                        storagePlace = storagePlace,
                        unit = unit,
                        productUrl = productUrl,
                        category = listOf(category),
                        createdBy = authService.getCurrUser()?.uid.orEmpty()
                    )
                )
                _success.emit("Product added successfully")
            }
        }
    }
}