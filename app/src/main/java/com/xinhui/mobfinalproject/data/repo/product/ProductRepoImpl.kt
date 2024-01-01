package com.xinhui.mobfinalproject.data.repo.product

import com.xinhui.mobfinalproject.data.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepoImpl: ProductRepo {
    override fun getAllProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun getProductById(id: String): Product {
        TODO("Not yet implemented")
    }

    override fun addNewProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override fun updateProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override fun deleteProduct(id: String) {
        TODO("Not yet implemented")
    }

}