package com.xinhui.mobfinalproject.data.model

data class Product(
    var id: Int?,
    var quantity: Int,
    var productName: String,
    var unit: String,
    var storagePlace: String,
    var category: String
) {
    fun toHash(): Map<String, Any?> {
        return hashMapOf(
            "quantity" to quantity,
            "productName" to productName,
            "unit" to unit,
            "storagePlace" to storagePlace,
            "category" to category,
        )
    }

    companion object {
        fun fromMap(hash: Map<String, Any?>): Product {
            return Product(
                id = hash["id"].toString().toInt(),
                quantity = hash["quantity"].toString().toInt(),
                productName = hash["productName"].toString(),
                unit = hash["unit"].toString(),
                storagePlace = hash["storagePlace"].toString(),
                category = hash["category"].toString(),
            )
        }
    }
}