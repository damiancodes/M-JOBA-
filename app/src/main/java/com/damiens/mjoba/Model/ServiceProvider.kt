package com.damiens.mjoba.Model

data class ServiceProvider(
    val userId: String = "",
    val businessName: String = "",
    val serviceCategories: List<String> = emptyList(),
    val description: String = "",
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val isVerified: Boolean = false,
    val isAvailable: Boolean = false,
    val location: GeoPoint? = null,
    val address: String = "", // Add this field for human-readable address
    val coverImageUrl: String = "",
    val galleryImages: List<String> = emptyList(),
    val services: List<Service> = emptyList()
)

data class GeoPoint(
    val latitude: Double,
    val longitude: Double
)