package com.damiens.mjoba.Data.remote



import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.messaging.FirebaseMessaging

object FirebaseModule {
    fun initialize(context: Context) {
        // Initialize Firebase
        FirebaseApp.initializeApp(context)
    }

    // Provide Firebase dependencies
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()
}