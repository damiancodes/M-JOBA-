package com.damiens.mjoba

import android.app.Application
import android.util.Log
import com.damiens.mjoba.Data.remote.FirebaseModule
import com.google.android.libraries.places.api.Places
import com.jakewharton.threetenabp.AndroidThreeTen

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("MJobaApp", "Application onCreate starting")

        // Initialize ThreeTenABP
        try {
            AndroidThreeTen.init(this)
            Log.d("MJobaApp", "ThreeTenABP initialized successfully")
        } catch (e: Exception) {
            Log.e("MJobaApp", "Error initializing ThreeTenABP: ${e.message}", e)
        }

        // Initialize Places API
        try {
            val apiKey = "AIzaSyAamvbmvSbFnO6aEM4A5J0oSzL6EHOLJCA"
            if (apiKey.isNotBlank()) {
                Places.initialize(applicationContext, apiKey)
                Log.d("MJobaApp", "Places API initialized successfully")
            } else {
                Log.e("MJobaApp", "Places API key is empty")
            }
        } catch (e: Exception) {
            Log.e("MJobaApp", "Error initializing Places API: ${e.message}", e)
        }

        // Initialize Firebase
        try {
            FirebaseModule.initialize(this)
            Log.d("MJobaApp", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("MJobaApp", "Error initializing Firebase: ${e.message}", e)
        }

        Log.d("MJobaApp", "Application onCreate completed")
    }
}