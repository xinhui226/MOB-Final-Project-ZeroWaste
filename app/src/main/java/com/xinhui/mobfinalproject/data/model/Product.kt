package com.xinhui.mobfinalproject.data.model

import com.xinhui.mobfinalproject.core.utils.Category

data class Product(
    var id: String? = "",
    var quantity: Int,
    var productName: String,
    var unit: String,
    var storagePlace: String,
    var expiryDate: String,
    var category: Category,
    var createdBy: String? = null
) {
    fun toHash(): Map<String, Any?> {
        return hashMapOf(
            "quantity" to quantity,
            "productName" to productName,
            "unit" to unit,
            "storagePlace" to storagePlace,
            "expiryDate" to expiryDate,
            "category" to category.categoryName,
            "createdBy" to createdBy
        )
    }

    companion object {
        fun fromMap(hash: Map<String, Any?>): Product {
            return Product(
                id = hash["id"].toString(),
                quantity = hash["quantity"].toString().toInt(),
                productName = hash["productName"].toString(),
                unit = hash["unit"].toString(),
                storagePlace = hash["storagePlace"].toString(),
                expiryDate = hash["expiryDate"].toString(),
                category = when(hash["category"].toString()){
                        "Dairy" -> Category.dairy
                        "Fruits" -> Category.fruits
                        "Cereals & Grains" -> Category.cerealsgrains
                        "Meat" -> Category.meat
                        "Confections" -> Category.confections
                        "Vegetables" -> Category.vegetables
                        else -> Category.drinks
                },
                createdBy = hash["createdBy"].toString()
            )
        }
    }
}