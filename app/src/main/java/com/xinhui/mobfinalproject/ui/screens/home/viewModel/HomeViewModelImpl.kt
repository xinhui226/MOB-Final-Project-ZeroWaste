package com.xinhui.mobfinalproject.ui.screens.home.viewModel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.xinhui.mobfinalproject.core.utils.AlarmManagerHelper
import com.xinhui.mobfinalproject.core.utils.Category
import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.data.repo.product.ProductRepo
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
    private val _selectedCat = MutableStateFlow(Category.all)
    val selectedCat: StateFlow<Category> = _selectedCat

    val job = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreateView() {
        super.onCreateView()
        if (_selectedCat.value == Category.all)
            getProducts()
        else
            getProducts(_selectedCat.value.categoryName)
    }

    override fun getProducts() {
        scope.launch(Dispatchers.IO) {
            _selectedCat.emit(Category.all)
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
        scope.launch(Dispatchers.IO) {
            Category.values().find { it.categoryName == category }?.let { _selectedCat.emit(it) }
            _isLoading.emit(true)
            errorHandler {
                productRepo.getProductsByCategory(category).collect {
                    _products.emit(it)
                    _isLoading.emit(false)
                }
            }
        }
    }

    fun stopJob() {
        job.cancel()
    }

    override fun deleteProduct(id: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler{ productRepo.deleteProduct(id) }
            AlarmManagerHelper.cancelAlarms(context,id)
        }
    }
}