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
    var createdBy: String? = null,
    var productUrl: String? = null
) {
    fun toHash(): Map<String, Any?> {
        return hashMapOf(
            "quantity" to quantity,
            "productName" to productName,
            "unit" to unit,
            "storagePlace" to storagePlace,
            "expiryDate" to expiryDate,
            "category" to category.categoryName,
            "createdBy" to createdBy,
            "productUrl" to productUrl
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
                        "Fruit" -> Category.fruits
                        "Grain & Cereal" -> Category.cerealsgrains
                        "Meat" -> Category.meat
                        "Confection" -> Category.confections
                        "Vegetable" -> Category.vegetables
                        "Poultry" -> Category.poultry
                        "Beverage" -> Category.drinks
                        else -> Category.others
                },
                createdBy = hash["createdBy"].toString(),
                productUrl = hash["productUrl"].toString()
            )
        }
    }
}