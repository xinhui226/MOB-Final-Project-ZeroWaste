package com.xinhui.mobfinalproject.ui.screens.home.viewModel

import androidx.lifecycle.viewModelScope
import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.data.repo.product.ProductRepo
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val productRepo: ProductRepo
): BaseViewModel(), HomeViewModel {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    override val products: StateFlow<List<Product>> = _products

    init {
        getProducts()
    }

    override fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                productRepo.getAllProducts().collect {
                    _products.emit(it)
                } }
        }
    }

    override fun getProductsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                productRepo.getProductsByCategory(category).collect {
                    _products.emit(it)
                } }
        }
    }
}