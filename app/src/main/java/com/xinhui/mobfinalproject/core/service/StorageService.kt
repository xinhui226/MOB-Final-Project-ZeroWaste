package com.xinhui.mobfinalproject.core.service

import android.net.Uri

interface StorageService {
    suspend fun addImage(name:String,uri: Uri): String
    suspend fun getImage(name: String): Uri?
}