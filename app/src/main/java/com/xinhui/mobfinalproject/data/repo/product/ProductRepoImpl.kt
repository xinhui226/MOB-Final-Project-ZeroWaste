package com.xinhui.mobfinalproject.data.repo.product

import com.xinhui.mobfinalproject.data.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepoImpl: ProductRepo {
    override suspend fun getAllProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductById(id: String): Product {
        TODO("Not yet implemented")
    }

    override suspend fun addNewProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(id: String) {
        TODO("Not yet implemented")
    }

}