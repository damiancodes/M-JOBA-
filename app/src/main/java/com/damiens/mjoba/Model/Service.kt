package com.damiens.mjoba.Model

data class Service(
    val id: String = "",
    val categoryId: String = "",
    val providerId: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val priceType: PriceType = PriceType.FIXED,
    val imageUrl: String = "",
    val estimatedDuration: Int = 60 // in minutes
)

enum class PriceType {
    FIXED, HOURLY, CUSTOM
}