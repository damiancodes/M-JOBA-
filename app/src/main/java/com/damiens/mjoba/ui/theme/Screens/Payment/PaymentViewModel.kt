
// PaymentViewModel.kt to connect UI with the repository
package com.damiens.mjoba.ui.theme.Screens.Payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damiens.mjoba.Data.PaymentRepository
import com.damiens.mjoba.Data.remote.MpesaResult
import com.damiens.mjoba.Model.Booking
import com.damiens.mjoba.Model.BookingStatus
import com.damiens.mjoba.Model.PaymentStatus
import com.damiens.mjoba.Model.Service
import com.damiens.mjoba.Model.ServiceProvider
import com.damiens.mjoba.util.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {
    private val repository = PaymentRepository()

    // UI state
    private val _uiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Initial)
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    // Payment details
    private var _booking: Booking? = null

    // Process payment with M-Pesa
    fun processPayment(
        phoneNumber: String,
        amount: Int,
        providerId: String,
        serviceId: String,
        date: String,
        time: String
    ) {
        // Load service details
        val service = SampleData.getService(serviceId)

        viewModelScope.launch {
            _uiState.value = PaymentUiState.Loading

            // First create a booking record
            val booking = repository.createBooking(
                customerId = "u123", // This would come from authentication
                serviceProviderId = providerId,
                serviceId = serviceId,
                date = date,
                time = time,
                totalAmount = amount.toDouble()
            )

            _booking = booking

            // Initiate M-Pesa payment
            val result = repository.processPayment(
                phoneNumber = phoneNumber,
                amount = amount,
                bookingId = booking.id,
                serviceDescription = service.name
            )

            when (result) {
                is MpesaResult.Success -> {
                    // Update booking with payment processing status
                    repository.updateBookingPaymentStatus(
                        bookingId = booking.id,
                        paymentStatus = PaymentStatus.PROCESSING
                    )

                    _uiState.value = PaymentUiState.Success(
                        amount = amount,
                        providerId = providerId,
                        serviceId = serviceId,
                        transactionId = result.checkoutRequestID
                    )
                }
                is MpesaResult.Error -> {
                    // Update booking with payment failed status
                    repository.updateBookingPaymentStatus(
                        bookingId = booking.id,
                        paymentStatus = PaymentStatus.FAILED,
                        bookingStatus = BookingStatus.CANCELLED
                    )

                    _uiState.value = PaymentUiState.Error(result.message)
                }
            }
        }
    }

    // Reset the payment state (e.g., after navigating away)
    fun resetState() {
        _uiState.value = PaymentUiState.Initial
    }
}

// UI state for payment screen
sealed class PaymentUiState {
    object Initial : PaymentUiState()
    object Loading : PaymentUiState()
    data class Success(
        val amount: Int,
        val providerId: String,
        val serviceId: String,
        val transactionId: String
    ) : PaymentUiState()
    data class Error(val message: String) : PaymentUiState()
}
