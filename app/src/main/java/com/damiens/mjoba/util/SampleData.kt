package com.damiens.mjoba.util

import com.damiens.mjoba.Model.*
import java.util.*

/**
 * Utility class providing sample data for development purposes.
 * This will be replaced by actual data from Firebase in production.
 */
object SampleData {
    fun getUser(userId: String?): User {
        return User(
            id = userId ?: "u123",
            name = "Damian padri",
            email = "Damien.padroe@example.com",
            phone = "+254 712 345 678",
            profileImageUrl = "",
            address = "123 Main St, Nairobi",
            isServiceProvider = false,
            dateCreated = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000) // 30 days ago
        )
    }

    fun getServiceProvider(providerId: String): ServiceProvider {
        return when (providerId) {
            "2001" -> ServiceProvider(
                userId = "u2001",
                businessName = "CleanHome Services",
                serviceCategories = listOf("101"),
                description = "We provide professional cleaning services for homes, offices and commercial spaces.",
                rating = 4.8f,
                reviewCount = 124,
                isVerified = true,
                isAvailable = true
            )
            "3001" -> ServiceProvider(
                userId = "u3001",
                businessName = "Glamour Hair Salon",
                serviceCategories = listOf("201"),
                description = "Professional hair styling for all occasions.",
                rating = 4.6f,
                reviewCount = 78,
                isVerified = true,
                isAvailable = true
            )
            else -> ServiceProvider(
                userId = "u${providerId.substring(1)}",
                businessName = "Service Provider $providerId",
                serviceCategories = listOf("101", "201", "301"),
                description = "Professional service provider with years of experience.",
                rating = 4.0f,
                reviewCount = 50,
                isVerified = true,
                isAvailable = true
            )
        }
    }


    fun getService(serviceId: String): Service {
        return when (serviceId) {
            "1001" -> Service(id = "1001", categoryId = "101", providerId = "2001", name = "Basic House Cleaning", description = "General cleaning of your home", price = 1500.0, estimatedDuration = 120)
            "2001" -> Service(id = "2001", categoryId = "201", providerId = "3001", name = "Hair Styling", description = "Professional styling for all occasions", price = 1000.0, estimatedDuration = 60)
            else -> Service(id = serviceId, categoryId = "101", providerId = "2001", name = "Service", description = "Service description", price = 1000.0, estimatedDuration = 60)
        }
    }

    fun getCategory(categoryId: String): ServiceCategory {
        return when (categoryId) {
            "101" -> ServiceCategory(id = "101", name = "House Cleaning", icon = "", description = "Professional house cleaning services for homes of all sizes.")
            "201" -> ServiceCategory(id = "201", name = "Women's Hair", icon = "", description = "Professional hair styling, cutting, coloring, and treatment services for women.")
            "301" -> ServiceCategory(id = "301", name = "Plumbing", icon = "", description = "Expert plumbers to fix leaks, install fixtures, and handle all your plumbing needs.")
            "401" -> ServiceCategory(id = "401", name = "Women's Clothing", icon = "", description = "Quality second-hand women's clothing from trusted wholesalers.")
            "501" -> ServiceCategory(id = "501", name = "Kitchen Supplies", icon = "", description = "Wholesale kitchen supplies including utensils, cookware, and appliances.")
            else -> ServiceCategory(id = categoryId, name = "Category", icon = "", description = "Category description.")
        }
    }
    // In SampleData.kt
    fun getBooking(bookingId: String): Booking? {
        return listOf(
            Booking(
                id = "b001",
                customerId = "u123",
                serviceProviderId = "2001",
                serviceId = "1001",
                date = "Mon, Apr 17",
                time = "10:00 AM",
                status = BookingStatus.CONFIRMED,
                totalAmount = 1575.0,
                paymentStatus = PaymentStatus.COMPLETED,
                createdAt = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000)
            ),
            // Add more bookings here
        ).find { it.id == bookingId }
    }
    // In SampleData.kt
    fun getServiceProvidersByCategory(categoryId: String): List<ServiceProvider> {
        // This is a sample implementation - in a real app this would query your database
        return when (categoryId) {
            "101" -> listOf( // House Cleaning
                ServiceProvider(
                    userId = "2001",
                    businessName = "CleanHome Services",
                    serviceCategories = listOf("101"),
                    description = "Professional cleaning services for homes and offices",
                    rating = 4.8f,
                    reviewCount = 124,
                    isVerified = true,
                    isAvailable = true
                ),
                // Other providers...
            )
            // Other categories...
            else -> listOf(
                ServiceProvider(
                    userId = "5001",
                    businessName = "General Service Provider",
                    serviceCategories = listOf(categoryId),
                    description = "Quality services for your needs",
                    rating = 4.2f,
                    reviewCount = 27,
                    isVerified = false,
                    isAvailable = true
                )
            )
        }
    }
}

