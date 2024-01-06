package com.xinhui.mobfinalproject.data.model

data class Product(
    var id: String? = "",
    var quantity: Int,
    var productName: String,
    var unit: String,
    var storagePlace: String,
    var expiryDate: String,
    var category: List<Category> = emptyList(),
    var createdBy: String
) {
    fun toHash(): Map<String, Any?> {
        return hashMapOf(
            "quantity" to quantity,
            "productName" to productName,
            "unit" to unit,
            "storagePlace" to storagePlace,
            "expiryDate" to expiryDate,
            "category" to category,
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
                category = (hash["category"] as ArrayList<*>).map {
                    when(it){
                        "Diary" -> Category.diary
                        "Fruits" -> Category.fruits
                        "Cereals & Grains" -> Category.cerealsgrains
                        "Meat" -> Category.meat
                        "Confections" -> Category.confections
                        "Vegetables" -> Category.vegetables
                        else -> Category.drinks
                } }.toList(),
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