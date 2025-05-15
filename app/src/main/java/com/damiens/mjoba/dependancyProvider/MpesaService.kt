package com.damiens.mjoba.dependancyProvider

import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Service class for M-Pesa API integration.
 * Handles authentication and STK push transactions.
 */
class MpesaService {
    companion object {
        // Replace these with your actual credentials from Safaricom Developer Portal
        private const val CONSUMER_KEY = "2wAKKr3EQVZCjoG64vGRxk6R7vkXZapUFNYcGQsbIAaMebSD"
        private const val CONSUMER_SECRET = "LVRxnPGajFSxe3EOuss7XRthQoZQKG8UBeVpbcGwgNWsbvRY1HyY5aZYjxSHZ195"
        private const val BUSINESS_SHORT_CODE = "174379" // This is the Lipa Na M-Pesa paybill for sandbox
        private const val PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919" // Get this from the Safaricom Developer Portal
        private const val TRANSACTION_TYPE = "CustomerPayBillOnline"

        // Base URLs - switch to production URLs when going live
        private const val AUTH_URL = "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials"
        private const val STK_PUSH_URL = "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest"

        // Callback URL - should point to your server that will receive the M-Pesa response
        private const val CALLBACK_URL = "https://yourdomain.com/mpesa/callback"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    /**
     * Generate access token for M-Pesa API authentication
     */
    private suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        try {
            val auth = "$CONSUMER_KEY:$CONSUMER_SECRET"
            val encodedAuth = Base64.encodeToString(auth.toByteArray(), Base64.NO_WRAP)

            val request = Request.Builder()
                .url(AUTH_URL)
                .addHeader("Authorization", "Basic $encodedAuth")
                .get()
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val jsonObject = JSONObject(responseBody)
                return@withContext jsonObject.getString("access_token")
            } else {
                Log.e("MpesaService", "Auth failed: ${response.code}")
                return@withContext null
            }
        } catch (e: Exception) {
            Log.e("MpesaService", "Auth exception: ${e.message}")
            return@withContext null
        }
    }

    /**
     * Generate timestamp for M-Pesa API in the required format
     */
    private fun getTimestamp(): String {
        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Generate password for M-Pesa API
     */
    private fun getPassword(timestamp: String): String {
        val str = "$BUSINESS_SHORT_CODE$PASSKEY$timestamp"
        return Base64.encodeToString(str.toByteArray(), Base64.NO_WRAP)
    }

    /**
     * Initiate STK push request to the customer's phone
     */
    suspend fun initiateSTKPush(
        phoneNumber: String,
        amount: Int,
        accountReference: String,
        description: String
    ): MpesaResult = withContext(Dispatchers.IO) {
        try {
            // Format phone number to required format (2547XXXXXXXX)
            val formattedPhone = formatPhoneNumber(phoneNumber)

            // Get access token
            val accessToken = getAccessToken()
            if (accessToken == null) {
                return@withContext MpesaResult.Error("Failed to authenticate with M-Pesa")
            }

            // Prepare timestamp
            val timestamp = getTimestamp()
            val password = getPassword(timestamp)

            // Create JSON payload
            val jsonPayload = JSONObject().apply {
                put("BusinessShortCode", BUSINESS_SHORT_CODE)
                put("Password", password)
                put("Timestamp", timestamp)
                put("TransactionType", TRANSACTION_TYPE)
                put("Amount", amount)
                put("PartyA", formattedPhone)
                put("PartyB", BUSINESS_SHORT_CODE)
                put("PhoneNumber", formattedPhone)
                put("CallBackURL", CALLBACK_URL)
                put("AccountReference", accountReference)
                put("TransactionDesc", description)
            }.toString()

            // Create request
            val requestBody = jsonPayload.toRequestBody("application/json".toMediaTypeOrNull())
            val request = Request.Builder()
                .url(STK_PUSH_URL)
                .addHeader("Authorization", "Bearer $accessToken")
                .post(requestBody)
                .build()

            // Execute request
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val jsonObject = JSONObject(responseBody)

                return@withContext if (jsonObject.has("ResponseCode") &&
                    jsonObject.getString("ResponseCode") == "0") {
                    // Success
                    val checkoutRequestID = jsonObject.getString("CheckoutRequestID")
                    MpesaResult.Success(
                        checkoutRequestID = checkoutRequestID,
                        merchantRequestID = jsonObject.getString("MerchantRequestID")
                    )
                } else {
                    // API returned an error
                    val errorMessage = if (jsonObject.has("errorMessage")) {
                        jsonObject.getString("errorMessage")
                    } else {
                        "Unknown error during payment processing"
                    }
                    MpesaResult.Error(errorMessage)
                }
            } else {
                // HTTP error
                return@withContext MpesaResult.Error(
                    "Network error: ${response.code}"
                )
            }
        } catch (e: Exception) {
            Log.e("MpesaService", "STK Push exception: ${e.message}")
            return@withContext MpesaResult.Error(
                "Payment processing failed: ${e.message}"
            )
        }
    }

    /**
     * Format phone number to required M-Pesa API format
     */
    private fun formatPhoneNumber(phoneNumber: String): String {
        // Remove any spaces, hyphens, or other non-digit characters
        var digits = phoneNumber.replace(Regex("[^0-9]"), "")

        // Handle different formats (e.g. 0722XXX, +254722XXX, 254722XXX)
        if (digits.startsWith("0")) {
            // Replace leading 0 with 254
            digits = "254" + digits.substring(1)
        } else if (digits.startsWith("+")) {
            // Remove leading +
            digits = digits.substring(1)
        } else if (!digits.startsWith("254")) {
            // Add country code if missing
            digits = "254$digits"
        }

        return digits
    }
}

/**
 * Sealed class to represent M-Pesa API results
 */
sealed class MpesaResult {
    data class Success(
        val checkoutRequestID: String,
        val merchantRequestID: String
    ) : MpesaResult()

    data class Error(val message: String) : MpesaResult()
}
