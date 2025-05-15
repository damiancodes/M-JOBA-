package com.damiens.mjoba.Data.remote

import com.damiens.mjoba.Model.GeoPoint
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ServiceProviderRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val providersCollection = firestore.collection("serviceProviders")

    // Register as service provider
    suspend fun registerAsServiceProvider(
        userId: String,
        businessName: String,
        serviceCategories: List<String>,
        description: String,
        address: String,
        location: GeoPoint?
    ): Result<ServiceProvider> {
        return try {
            // Create map of fields to save to Firestore
            val providerData = hashMapOf(
                "userId" to userId,
                "businessName" to businessName,
                "serviceCategories" to serviceCategories,
                "description" to description,
                "address" to address,
                "location" to location,
                "isVerified" to false, // Providers start unverified
                "isAvailable" to true, // Default to available
                "rating" to 0f,
                "reviewCount" to 0,
                "coverImageUrl" to "",
                "galleryImages" to emptyList<String>()
            )




            // Save to Firestore
            providersCollection.document(userId).set(providerData).await()

            // Also update the user record to mark as service provider
            firestore.collection("users").document(userId)
                .update("isServiceProvider", true).await()

            // Create ServiceProvider object to return
            val serviceProvider = ServiceProvider(
                userId = userId,
                businessName = businessName,
                serviceCategories = serviceCategories,
                description = description,
                address = address,
                location = location,
                isVerified = false,
                isAvailable = true
            )

            Result.success(serviceProvider)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get service provider by userId
    suspend fun getServiceProviderByUserId(userId: String): Result<ServiceProvider> {
        return try {
            val document = providersCollection.document(userId).get().await()

            if (document.exists()) {
                // Get the data as a map since we can't directly deserialize
                val data = document.data
                if (data != null) {
                    // Manually create ServiceProvider object from map
                    val provider = ServiceProvider(
                        userId = userId,
                        businessName = data["businessName"] as? String ?: "",
                        serviceCategories = (data["serviceCategories"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                        description = data["description"] as? String ?: "",
                        rating = (data["rating"] as? Number)?.toFloat() ?: 0f,
                        reviewCount = (data["reviewCount"] as? Number)?.toInt() ?: 0,
                        isVerified = data["isVerified"] as? Boolean ?: false,
                        isAvailable = data["isAvailable"] as? Boolean ?: false,
                        location = data["location"]?.let {
                            val locationMap = it as? Map<*, *>
                            if (locationMap != null) {
                                val latitude = (locationMap["latitude"] as? Number)?.toDouble() ?: 0.0
                                val longitude = (locationMap["longitude"] as? Number)?.toDouble() ?: 0.0
                                GeoPoint(latitude, longitude)
                            } else null
                        },
                        address = data["address"] as? String ?: "",
                        coverImageUrl = data["coverImageUrl"] as? String ?: "",
                        galleryImages = (data["galleryImages"] as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                    )
                    Result.success(provider)
                } else {
                    Result.failure(Exception("Provider data is null"))
                }
            } else {
                Result.failure(Exception("Service provider not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update service provider profile
    suspend fun updateServiceProviderProfile(
        userId: String,
        businessName: String? = null,
        serviceCategories: List<String>? = null,
        description: String? = null,
        address: String? = null,
        location: GeoPoint? = null,
        coverImageUrl: String? = null,
        galleryImages: List<String>? = null,
        isAvailable: Boolean? = null
    ): Result<ServiceProvider> {
        val providerRef = providersCollection.document(userId)

        return try {
            // Create map of fields to update
            val updates = mutableMapOf<String, Any?>()

            businessName?.let { updates["businessName"] = it }
            serviceCategories?.let { updates["serviceCategories"] = it }
            description?.let { updates["description"] = it }
            address?.let { updates["address"] = it }
            location?.let { updates["location"] = it }
            coverImageUrl?.let { updates["coverImageUrl"] = it }
            galleryImages?.let { updates["galleryImages"] = it }
            isAvailable?.let { updates["isAvailable"] = it }

            // Update in Firestore
            providerRef.update(updates).await()

            // Get the updated document
            val updatedDoc = providerRef.get().await()
            val updatedData = updatedDoc.data

            if (updatedData != null) {
                // Manually create ServiceProvider object from map
                val provider = ServiceProvider(
                    userId = userId,
                    businessName = updatedData["businessName"] as? String ?: "",
                    serviceCategories = (updatedData["serviceCategories"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                    description = updatedData["description"] as? String ?: "",
                    rating = (updatedData["rating"] as? Number)?.toFloat() ?: 0f,
                    reviewCount = (updatedData["reviewCount"] as? Number)?.toInt() ?: 0,
                    isVerified = updatedData["isVerified"] as? Boolean ?: false,
                    isAvailable = updatedData["isAvailable"] as? Boolean ?: false,
                    location = updatedData["location"]?.let {
                        val locationMap = it as? Map<*, *>
                        if (locationMap != null) {
                            val latitude = (locationMap["latitude"] as? Number)?.toDouble() ?: 0.0
                            val longitude = (locationMap["longitude"] as? Number)?.toDouble() ?: 0.0
                            GeoPoint(latitude, longitude)
                        } else null
                    },
                    address = updatedData["address"] as? String ?: "",
                    coverImageUrl = updatedData["coverImageUrl"] as? String ?: "",
                    galleryImages = (updatedData["galleryImages"] as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                )
                Result.success(provider)
            } else {
                Result.failure(Exception("Updated provider data is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get all service providers
    suspend fun getAllServiceProviders(): Result<List<ServiceProvider>> {
        return try {
            val snapshot = providersCollection.get().await()
            val providers = snapshot.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null

                ServiceProvider(
                    userId = doc.id,
                    businessName = data["businessName"] as? String ?: "",
                    serviceCategories = (data["serviceCategories"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                    description = data["description"] as? String ?: "",
                    rating = (data["rating"] as? Number)?.toFloat() ?: 0f,
                    reviewCount = (data["reviewCount"] as? Number)?.toInt() ?: 0,
                    isVerified = data["isVerified"] as? Boolean ?: false,
                    isAvailable = data["isAvailable"] as? Boolean ?: false,
                    location = data["location"]?.let {
                        val locationMap = it as? Map<*, *>
                        if (locationMap != null) {
                            val latitude = (locationMap["latitude"] as? Number)?.toDouble() ?: 0.0
                            val longitude = (locationMap["longitude"] as? Number)?.toDouble() ?: 0.0
                            GeoPoint(latitude, longitude)
                        } else null
                    },
                    address = data["address"] as? String ?: "",
                    coverImageUrl = data["coverImageUrl"] as? String ?: "",
                    galleryImages = (data["galleryImages"] as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                )
            }
            Result.success(providers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get service providers by category
    suspend fun getServiceProvidersByCategory(categoryId: String): Result<List<ServiceProvider>> {
        return try {
            val snapshot = providersCollection
                .whereArrayContains("serviceCategories", categoryId)
                .get()
                .await()

            val providers = snapshot.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null

                ServiceProvider(
                    userId = doc.id,
                    businessName = data["businessName"] as? String ?: "",
                    serviceCategories = (data["serviceCategories"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                    description = data["description"] as? String ?: "",
                    rating = (data["rating"] as? Number)?.toFloat() ?: 0f,
                    reviewCount = (data["reviewCount"] as? Number)?.toInt() ?: 0,
                    isVerified = data["isVerified"] as? Boolean ?: false,
                    isAvailable = data["isAvailable"] as? Boolean ?: false,
                    location = data["location"]?.let {
                        val locationMap = it as? Map<*, *>
                        if (locationMap != null) {
                            val latitude = (locationMap["latitude"] as? Number)?.toDouble() ?: 0.0
                            val longitude = (locationMap["longitude"] as? Number)?.toDouble() ?: 0.0
                            GeoPoint(latitude, longitude)
                        } else null
                    },
                    address = data["address"] as? String ?: "",
                    coverImageUrl = data["coverImageUrl"] as? String ?: "",
                    galleryImages = (data["galleryImages"] as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                )
            }
            Result.success(providers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Add a new service to provider
    suspend fun addService(providerId: String, service: Service): Result<Service> {
        return try {
            // Generate unique ID for the service
            val serviceId = firestore.collection("services").document().id

            // Create map of service data
            val serviceData = hashMapOf(
                "id" to serviceId,
                "providerId" to providerId,
                "categoryId" to service.categoryId,
                "name" to service.name,
                "description" to service.description,
                "price" to service.price,
                "priceType" to service.priceType.toString(),
                "imageUrl" to service.imageUrl,
                "estimatedDuration" to service.estimatedDuration
            )

            // Save service to Firestore
            firestore.collection("services").document(serviceId)
                .set(serviceData).await()

            // Create service object to return
            val serviceWithId = service.copy(id = serviceId, providerId = providerId)
            Result.success(serviceWithId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get all services for a provider
    suspend fun getProviderServices(providerId: String): Result<List<Service>> {
        return try {
            val snapshot = firestore.collection("services")
                .whereEqualTo("providerId", providerId)
                .get()
                .await()

            val services = snapshot.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null

                try {
                    Service(
                        id = doc.id,
                        providerId = data["providerId"] as? String ?: "",
                        categoryId = data["categoryId"] as? String ?: "",
                        name = data["name"] as? String ?: "",
                        description = data["description"] as? String ?: "",
                        price = (data["price"] as? Number)?.toDouble() ?: 0.0,
                        priceType = try {
                            val priceTypeStr = data["priceType"] as? String ?: ""
                            com.damiens.mjoba.Model.PriceType.valueOf(priceTypeStr)
                        } catch (e: Exception) {
                            com.damiens.mjoba.Model.PriceType.FIXED
                        },
                        imageUrl = data["imageUrl"] as? String ?: "",
                        estimatedDuration = (data["estimatedDuration"] as? Number)?.toInt() ?: 60
                    )
                } catch (e: Exception) {
                    null
                }
            }
            Result.success(services)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // Add this method to your ServiceProviderRepository class in ServiceProviderRepository.kt
    suspend fun getAllServices(): Result<List<Service>> {
        return try {
            val snapshot = firestore.collection("services").get().await()
            val services = snapshot.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null

                try {
                    Service(
                        id = doc.id,
                        providerId = data["providerId"] as? String ?: "",
                        categoryId = data["categoryId"] as? String ?: "",
                        name = data["name"] as? String ?: "",
                        description = data["description"] as? String ?: "",
                        price = (data["price"] as? Number)?.toDouble() ?: 0.0,
                        priceType = try {
                            val priceTypeStr = data["priceType"] as? String ?: ""
                            com.damiens.mjoba.Model.PriceType.valueOf(priceTypeStr)
                        } catch (e: Exception) {
                            com.damiens.mjoba.Model.PriceType.FIXED
                        },
                        imageUrl = data["imageUrl"] as? String ?: "",
                        estimatedDuration = (data["estimatedDuration"] as? Number)?.toInt() ?: 60
                    )
                } catch (e: Exception) {
                    null
                }
            }
            Result.success(services)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update provider verification status (for admin use)
    suspend fun updateVerificationStatus(
        providerId: String,
        isVerified: Boolean
    ): Result<Boolean> {
        return try {
            providersCollection.document(providerId)
                .update("isVerified", isVerified)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Search providers by name
    suspend fun searchProvidersByName(query: String): Result<List<ServiceProvider>> {
        return try {
            // Firestore doesn't support LIKE queries directly, so we'll use >= and < for prefix matching
            val snapshot = providersCollection
                .orderBy("businessName")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .await()

            val providers = snapshot.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null

                ServiceProvider(
                    userId = doc.id,
                    businessName = data["businessName"] as? String ?: "",
                    serviceCategories = (data["serviceCategories"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                    description = data["description"] as? String ?: "",
                    rating = (data["rating"] as? Number)?.toFloat() ?: 0f,
                    reviewCount = (data["reviewCount"] as? Number)?.toInt() ?: 0,
                    isVerified = data["isVerified"] as? Boolean ?: false,
                    isAvailable = data["isAvailable"] as? Boolean ?: false,
                    location = data["location"]?.let {
                        val locationMap = it as? Map<*, *>
                        if (locationMap != null) {
                            val latitude = (locationMap["latitude"] as? Number)?.toDouble() ?: 0.0
                            val longitude = (locationMap["longitude"] as? Number)?.toDouble() ?: 0.0
                            GeoPoint(latitude, longitude)
                        } else null
                    },
                    address = data["address"] as? String ?: "",
                    coverImageUrl = data["coverImageUrl"] as? String ?: "",
                    galleryImages = (data["galleryImages"] as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                )
            }

            Result.success(providers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}