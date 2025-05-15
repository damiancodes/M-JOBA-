package com.damiens.mjoba.Model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val profileImageUrl: String = "",
    val address: String = "",
    val location: GeoPoint? = null, // Add this field for coordinates
    val isServiceProvider: Boolean = false,
    val dateCreated: Long = System.currentTimeMillis(),
    val isAdmin: Boolean = false
)