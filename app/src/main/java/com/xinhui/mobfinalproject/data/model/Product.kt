package com.xinhui.mobfinalproject.data.model

data class Product(
    val id: String? = "",
    val quantity: Int,
    val productName: String,
    val unit: String,
    val storagePlace: String,
    val expiryDate: String,
    val category: Category,
    val createdBy: String? = null
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
                        "Diary" -> Category.diary
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

enum class Category(var categoryName:String) {
    diary("Diary"),
    fruits("Fruits"),
    cerealsgrains("Cereals & Grains"),
    meat("Meat"),
    confections("Confections"),
    vegetables("Vegetables"),
    drinks("Beverages")
}
