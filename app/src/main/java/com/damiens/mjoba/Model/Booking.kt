package com.damiens.mjoba.Model

// In Booking.kt
data class Booking(
    val id: String = "",
    val customerId: String = "",
    val serviceProviderId: String = "",
    val serviceId: String = "",
    val date: String = "",
    val time: String = "",
    val status: BookingStatus = BookingStatus.PENDING,
    val totalAmount: Double = 0.0,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis()
)

enum class BookingStatus {
    PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, REJECTED
}

//enum class PaymentStatus {
//    PENDING, PAID, FAILED, REFUNDED
//}

enum class PaymentStatus {
    PENDING, PROCESSING, COMPLETED, FAILED
}