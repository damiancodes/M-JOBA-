package com.damiens.mjoba.Data.remote

import com.damiens.mjoba.Model.ServiceCategory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ServiceCategoryRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val categoriesCollection = firestore.collection("serviceCategories")

    // Get all service categories
    suspend fun getAllCategories(): Result<List<ServiceCategory>> {
        return try {
            val snapshot = categoriesCollection
                .orderBy("name")
                .get()
                .await()

            val categories = snapshot.toObjects(ServiceCategory::class.java)
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get category by ID
    suspend fun getCategoryById(categoryId: String): Result<ServiceCategory> {
        return try {
            val document = categoriesCollection.document(categoryId).get().await()

            if (document.exists()) {
                val category = document.toObject(ServiceCategory::class.java)
                    ?: throw Exception("Failed to parse category")
                Result.success(category)
            } else {
                Result.failure(Exception("Category not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Add a new category (admin function)
    suspend fun addCategory(category: ServiceCategory): Result<ServiceCategory> {
        return try {
            // Generate ID if not provided
            val categoryId = category.id.ifEmpty {
                categoriesCollection.document().id
            }

            val categoryWithId = category.copy(id = categoryId)

            // Save to Firestore
            categoriesCollection.document(categoryId).set(categoryWithId).await()

            Result.success(categoryWithId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update a category (admin function)
    suspend fun updateCategory(categoryId: String, updates: Map<String, Any>): Result<Boolean> {
        return try {
            categoriesCollection.document(categoryId)
                .update(updates)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
