package com.xinhui.mobfinalproject.data.model

data class Product(
    var id: String? = "",
    var quantity: Int,
    var productName: String,
    var unit: String,
    var storagePlace: String,
    var expiryDate: String,
    var category: Category,
    var createdBy: String,
    var productUrl: String
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
                        "Diary" -> Category.diary
                        "Fruits" -> Category.fruits
                        "Cereals & Grains" -> Category.cerealsgrains
                        "Meat" -> Category.meat
                        "Confections" -> Category.confections
                        "Vegetables" -> Category.vegetables
                        else -> Category.drinks
                },
                productUrl = hash["productUrl"].toString(),
                createdBy = hash["createdBy"].toString()
            )
        }
    }
}

enum class Category(var categoryName:String) {
    diary("Diary"),
    fruits("Fruits"),
    cerealsgrains("Cereals & Grains"),
    meat("Meat"),
    confections("Confections"),
    vegetables("Vegetables"),
    drinks("Beverages")
}