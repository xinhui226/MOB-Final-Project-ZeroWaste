package com.xinhui.mobfinalproject.data.model

data class User(
    val id:String? = null,
    val name:String,
    val email:String,
    val profileUrl:String? = null,
){
    fun toHash():HashMap<String,Any>{
        return hashMapOf(
            "name" to name,
            "email" to email,
            "profileUrl" to profileUrl.toString(),
        )
    }

    companion object{
        fun fromHashMap(hash:Map<String,Any>):User{
            return User(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                email = hash["email"].toString(),
                profileUrl = hash["profileUrl"].toString(),
            )
        }
    }
}