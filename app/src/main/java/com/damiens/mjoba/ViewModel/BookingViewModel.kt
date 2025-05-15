package com.damiens.mjoba.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damiens.mjoba.Data.remote.BookingRepository
import com.damiens.mjoba.Model.Booking
import com.damiens.mjoba.Model.BookingStatus
import com.damiens.mjoba.Model.PaymentStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.internal.bind.TypeAdapters.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import com.damiens.mjoba.ViewModel.BookingViewModel

class BookingViewModel : ViewModel() {
    private val bookingRepository = BookingRepository()

    // UI state for booking process
    private val _bookingState = MutableStateFlow<BookingUiState>(BookingUiState.Initial)
    val bookingState: StateFlow<BookingUiState> = _bookingState.asStateFlow()



    // UI state for user's bookings list
    private val _userBookingsState = MutableStateFlow<UserBookingsUiState>(UserBookingsUiState.Initial)
    val userBookingsState: StateFlow<UserBookingsUiState> = _userBookingsState.asStateFlow()

    // Create a new booking
    fun createBooking(
        customerId: String,
        serviceProviderId: String,
        serviceId: String,
        date: String,
        time: String,
        totalAmount: Double
    ) {
        _bookingState.value = BookingUiState.Loading

        viewModelScope.launch {
            val result = bookingRepository.createBooking(
                customerId = customerId,
                serviceProviderId = serviceProviderId,
                serviceId = serviceId,
                date = date,
                time = time,
                totalAmount = totalAmount
            )

            result.fold(
                onSuccess = { booking ->
                    _bookingState.value = BookingUiState.Success(booking)
                },
                onFailure = { exception ->
                    _bookingState.value = BookingUiState.Error(exception.message ?: "Booking failed")
                }
            )
        }
    }

    // Update booking payment status
    fun updatePaymentStatus(bookingId: String, paymentStatus: PaymentStatus) {
        viewModelScope.launch {
            val result = bookingRepository.updateBookingPaymentStatus(
                bookingId = bookingId,
                paymentStatus = paymentStatus
            )

            result.fold(
                onSuccess = { success ->
                    // Refresh booking details
                    getBookingDetails(bookingId)
                },
                onFailure = { exception ->
                    _bookingState.value = BookingUiState.Error(exception.message ?: "Failed to update payment status")
                }
            )
        }
    }

    // Update booking status
    fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        viewModelScope.launch {
            val result = bookingRepository.updateBookingStatus(
                bookingId = bookingId,
                status = status
            )

            result.fold(
                onSuccess = { success ->
                    // Refresh booking details
                    getBookingDetails(bookingId)
                },
                onFailure = { exception ->
                    _bookingState.value = BookingUiState.Error(exception.message ?: "Failed to update booking status")
                }
            )
        }
    }

    // Get booking details
    fun getBookingDetails(bookingId: String) {
        _bookingState.value = BookingUiState.Loading

        viewModelScope.launch {
            val result = bookingRepository.getBookingById(bookingId)

            result.fold(
                onSuccess = { booking ->
                    _bookingState.value = BookingUiState.Success(booking)
                },
                onFailure = { exception ->
                    _bookingState.value = BookingUiState.Error(exception.message ?: "Failed to load booking details")
                }
            )
        }
    }

    // Get customer bookings
    fun getCustomerBookings(customerId: String) {
        _userBookingsState.value = UserBookingsUiState.Loading

        viewModelScope.launch {
            val result = bookingRepository.getCustomerBookings(customerId)

            result.fold(
                onSuccess = { bookings ->
                    _userBookingsState.value = UserBookingsUiState.Success(bookings)
                },
                onFailure = { exception ->
                    _userBookingsState.value = UserBookingsUiState.Error(exception.message ?: "Failed to load bookings")
                }
            )
        }
    }

    // Add these to your BookingViewModel class
    private val _bookingDetailsState = MutableStateFlow<BookingDetailsUiState>(BookingDetailsUiState.Initial)
    val bookingDetailsState: StateFlow<BookingDetailsUiState> = _bookingDetailsState.asStateFlow()

    // Get booking by ID
    fun getBookingById(bookingId: String) {
        _bookingDetailsState.value = BookingDetailsUiState.Loading

        viewModelScope.launch {
            try {
                val result = bookingRepository.getBookingById(bookingId)

                result.fold(
                    onSuccess = { booking ->
                        _bookingDetailsState.value = BookingDetailsUiState.Success(booking)
                    },
                    onFailure = { exception ->
                        _bookingDetailsState.value = BookingDetailsUiState.Error(
                            exception.message ?: "Failed to load booking details"
                        )
                    }
                )
            } catch (e: Exception) {
                _bookingDetailsState.value = BookingDetailsUiState.Error(
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    // Cancel booking
    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            try {
                val result = bookingRepository.updateBookingStatus(
                    bookingId = bookingId,
                    status = BookingStatus.CANCELLED
                )

                result.fold(
                    onSuccess = { success ->
                        if (success) {
                            // Refresh booking details to show updated status
                            getBookingById(bookingId)
                        }
                    },
                    onFailure = { exception ->
                        // Handle error - perhaps update a separate UI state for operations
                    }
                )
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // Define BookingDetailsUiState
    sealed class BookingDetailsUiState {
        object Initial : BookingDetailsUiState()
        object Loading : BookingDetailsUiState()
        data class Success(val booking: Booking) : BookingDetailsUiState()
        data class Error(val message: String) : BookingDetailsUiState()
    }

    // Get provider bookings
    fun getProviderBookings(providerId: String) {
        _userBookingsState.value = UserBookingsUiState.Loading

        viewModelScope.launch {
            val result = bookingRepository.getProviderBookings(providerId)

            result.fold(
                onSuccess = { bookings ->
                    _userBookingsState.value = UserBookingsUiState.Success(bookings)
                },
                onFailure = { exception ->
                    _userBookingsState.value = UserBookingsUiState.Error(exception.message ?: "Failed to load bookings")
                }
            )
        }
    }


    // Add this variable if it doesn't exist
    private val _temporaryBooking = MutableStateFlow<Booking?>(null)
    val temporaryBooking: StateFlow<Booking?> = _temporaryBooking.asStateFlow()

    // Add this method
    fun createTemporaryBooking(
        serviceId: String,
        providerId: String,
        date: String,
        time: String,
        location: String,
        phoneNumber: String,
        instructions: String,
        amount: Double
    ) {
        val booking = Booking(
            id = "${System.currentTimeMillis()}-${(1000..9999).random()}",   // Temporary ID
            customerId = getCurrentUserId() ?: "",
            serviceProviderId = providerId,
            serviceId = serviceId,
            date = date,
            time = time,
            location = location,
            contactPhone = phoneNumber,
            specialInstructions = instructions,
            status = BookingStatus.PENDING,
            totalAmount = amount,
            paymentStatus = PaymentStatus.PENDING,
            createdAt = System.currentTimeMillis()
        )

        _temporaryBooking.value = booking
    }

    // Helper method to get current user ID
    private fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    // Reset booking state (e.g., after navigation)
    fun resetBookingState() {
        _bookingState.value = BookingUiState.Initial
    }
}

// Sealed classes for UI states
sealed class BookingUiState {
    object Initial : BookingUiState()
    object Loading : BookingUiState()
    data class Success(val booking: Booking) : BookingUiState()
    data class Error(val message: String) : BookingUiState()
}

sealed class UserBookingsUiState {
    object Initial : UserBookingsUiState()
    object Loading : UserBookingsUiState()
    data class Success(val bookings: List<Booking>) : UserBookingsUiState()
    data class Error(val message: String) : UserBookingsUiState()
}

