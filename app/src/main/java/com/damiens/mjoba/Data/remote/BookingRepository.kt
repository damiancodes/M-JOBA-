package com.damiens.mjoba.Data.remote

import com.damiens.mjoba.Model.Booking
import com.damiens.mjoba.Model.BookingStatus
import com.damiens.mjoba.Model.PaymentStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.UUID

class BookingRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val bookingsCollection = firestore.collection("bookings")

    // Create a new booking
    suspend fun createBooking(
        customerId: String,
        serviceProviderId: String,
        serviceId: String,
        date: String,
        time: String,
        totalAmount: Double
    ): Result<Booking> {
        return try {
            // Generate a unique booking ID
            val bookingId = UUID.randomUUID().toString()

            val booking = Booking(
                id = bookingId,
                customerId = customerId,
                serviceProviderId = serviceProviderId,
                serviceId = serviceId,
                date = date,
                time = time,
                status = BookingStatus.PENDING,
                totalAmount = totalAmount,
                paymentStatus = PaymentStatus.PENDING,
                createdAt = System.currentTimeMillis()
            )

            // Save to Firestore
            bookingsCollection.document(bookingId).set(booking).await()

            Result.success(booking)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update booking payment status
    suspend fun updateBookingPaymentStatus(
        bookingId: String,
        paymentStatus: PaymentStatus,
        bookingStatus: BookingStatus = BookingStatus.CONFIRMED
    ): Result<Boolean> {
        return try {
            val updates = mapOf(
                "paymentStatus" to paymentStatus,
                "status" to bookingStatus
            )

            bookingsCollection.document(bookingId)
                .update(updates)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update booking status
    suspend fun updateBookingStatus(
        bookingId: String,
        status: BookingStatus
    ): Result<Boolean> {
        return try {
            bookingsCollection.document(bookingId)
                .update("status", status)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get booking by ID
    suspend fun getBookingById(bookingId: String): Result<Booking> {
        return try {
            val document = bookingsCollection.document(bookingId).get().await()

            if (document.exists()) {
                val booking = document.toObject(Booking::class.java)
                    ?: throw Exception("Failed to parse booking")
                Result.success(booking)
            } else {
                Result.failure(Exception("Booking not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get customer bookings
    suspend fun getCustomerBookings(customerId: String): Result<List<Booking>> {
        return try {
            val snapshot = bookingsCollection
                .whereEqualTo("customerId", customerId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val bookings = snapshot.toObjects(Booking::class.java)
            Result.success(bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get service provider bookings
    suspend fun getProviderBookings(providerId: String): Result<List<Booking>> {
        return try {
            val snapshot = bookingsCollection
                .whereEqualTo("serviceProviderId", providerId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val bookings = snapshot.toObjects(Booking::class.java)
            Result.success(bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get recent bookings for a provider
    suspend fun getRecentProviderBookings(
        providerId: String,
        limit: Long = 10
    ): Result<List<Booking>> {
        return try {
            val snapshot = bookingsCollection
                .whereEqualTo("serviceProviderId", providerId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            val bookings = snapshot.toObjects(Booking::class.java)
            Result.success(bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

