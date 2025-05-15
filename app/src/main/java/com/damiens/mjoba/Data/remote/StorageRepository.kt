package com.damiens.mjoba.Data.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StorageRepository {
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    // Upload user profile image
    suspend fun uploadUserProfileImage(userId: String, imageUri: Uri): Result<String> {
        return uploadImage("users/$userId/profile.jpg", imageUri)
    }

    // Upload service provider cover image
    suspend fun uploadProviderCoverImage(providerId: String, imageUri: Uri): Result<String> {
        return uploadImage("providers/$providerId/cover.jpg", imageUri)
    }

    // Upload service provider gallery image
    suspend fun uploadProviderGalleryImage(providerId: String, imageUri: Uri): Result<String> {
        val filename = "gallery_${UUID.randomUUID()}.jpg"
        return uploadImage("providers/$providerId/gallery/$filename", imageUri)
    }

    // Upload service image
    suspend fun uploadServiceImage(serviceId: String, imageUri: Uri): Result<String> {
        return uploadImage("services/$serviceId/image.jpg", imageUri)
    }

    // Generic image upload function
    private suspend fun uploadImage(path: String, imageUri: Uri): Result<String> {
        return try {
            val ref = storageRef.child(path)
            val uploadTask = ref.putFile(imageUri).await()

            // Get download URL
            val downloadUrl = ref.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Delete an image by URL
    suspend fun deleteImage(imageUrl: String): Result<Boolean> {
        return try {
            // Extract the path from the URL
            val path = storage.getReferenceFromUrl(imageUrl)
            path.delete().await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

