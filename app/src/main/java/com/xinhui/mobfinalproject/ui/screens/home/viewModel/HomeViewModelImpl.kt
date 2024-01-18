package com.xinhui.mobfinalproject.ui.screens.home.viewModel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.xinhui.mobfinalproject.core.utils.AlarmManagerHelper
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

    override fun onCreateView() {
        super.onCreateView()
        getProducts()
    }

    override fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            errorHandler {
                productRepo.getAllProducts().collect {
                    _products.emit(it)
                    _isLoading.emit(false)
                }
            }
        }
    }

    override fun getProducts(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            errorHandler {
                productRepo.getProductsByCategory(category).collect {
                    _products.emit(it)
                    _isLoading.emit(false)
                }
            }
        }
    }

    override fun deleteProduct(id: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler{ productRepo.deleteProduct(id) }
            AlarmManagerHelper.cancelAlarms(context,id)
        }
    }
}