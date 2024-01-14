package com.xinhui.mobfinalproject.core.service

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class StorageServiceImpl(
    private val storage: StorageReference = FirebaseStorage.getInstance().reference
):StorageService {
    override suspend fun addImage(name: String, uri: Uri): String {
        storage.child(name).putFile(uri).await()
        val url = storage.child(name).downloadUrl.await()
        return url.toString()
    }

    override suspend fun getImage(name: String): Uri? {
        return try {
            storage.child(name).downloadUrl.await()
        }catch (e:Exception) {
            e.printStackTrace()
            null
        }
    }
}