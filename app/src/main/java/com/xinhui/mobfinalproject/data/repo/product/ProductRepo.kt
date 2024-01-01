package com.xinhui.mobfinalproject.data.repo.product

import com.xinhui.mobfinalproject.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepo {
    fun getAllProducts(): Flow<List<Product>>
    fun getProductById(id: String): Product
    fun addNewProduct(product: Product)
    fun updateProduct(product: Product)
    fun deleteProduct(id: String)
}