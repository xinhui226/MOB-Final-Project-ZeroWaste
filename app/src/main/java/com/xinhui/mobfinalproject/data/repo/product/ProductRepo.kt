package com.xinhui.mobfinalproject.data.repo.product

import com.xinhui.mobfinalproject.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepo {
    suspend fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductById(id: String): Product
    suspend fun addNewProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(id: String)
}