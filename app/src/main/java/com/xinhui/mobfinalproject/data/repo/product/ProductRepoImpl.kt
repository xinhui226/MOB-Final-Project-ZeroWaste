package com.xinhui.mobfinalproject.data.repo.product

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xinhui.mobfinalproject.core.service.AuthService
import com.xinhui.mobfinalproject.data.model.Product
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProductRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
): ProductRepo {

    private fun getDBRef(): CollectionReference {
        return db.collection("products")
    }
    override suspend fun getAllProducts() = callbackFlow {
        val listener =
            getDBRef()
                .whereEqualTo("createdBy", authService.getUid())
                .orderBy("expiryDate")
                .addSnapshotListener { value, error ->
                    if(error != null) {
                        throw error
                    }
                    val products = mutableListOf<Product>()
                    value?.documents?.let { docs ->
                        for (doc in docs){
                            doc.data?.let {
                                it["id"] = doc.id
                                products.add(Product.fromMap(it))
                            }
                        } //end for (doc in docs)
                        trySend(products)
                    } //end value?.documents?
                } //end listener = query.addSnapshotListener
        awaitClose{
            listener.remove()
        }
    }

    override suspend fun getProductsByCategory(category: String) = callbackFlow {
        val listener =
            getDBRef()
                .whereEqualTo("createdBy", authService.getUid())
                .orderBy("expiryDate")
                .whereEqualTo("category", category)
                .addSnapshotListener { value, error ->
                    if(error != null) {
                        throw error
                    }
                    val products = mutableListOf<Product>()
                    value?.documents?.let { docs ->
                        for (doc in docs){
                            doc.data?.let {
                                it["id"] = doc.id
                                products.add(Product.fromMap(it))
                            }
                        } //end for (doc in docs)
                        trySend(products)
                    } //end value?.documents?
                } //end listener = query.addSnapshotListener
        awaitClose{
            listener.remove()
        }
    }

    override suspend fun getProductById(id: String): Product? {
        val doc = getDBRef().document(id).get().await()
        return doc.data?.let {
            it["id"] = doc.id
            Product.fromMap(it)
        }
    }

    override suspend fun addNewProduct(product: Product): String {
        val doc = getDBRef().add(
            Product(
                quantity = product.quantity,
                productName = product.productName,
                unit = product.unit,
                storagePlace = product.storagePlace,
                expiryDate = product.expiryDate,
                category = product.category,
                createdBy = authService.getUid()
            ).toHash()
        ).await()
        return doc.id
    }

    override suspend fun updateProduct(product: Product) {
        product.id.let {
            if (it.isNullOrEmpty()) throw Exception("Product id not found")
            else getDBRef().document(it).set(product.copy(createdBy = authService.getUid()).toHash()).await()
        }
    }

    override suspend fun deleteProduct(id: String) {
        getDBRef().document(id).delete().await()
    }

}
