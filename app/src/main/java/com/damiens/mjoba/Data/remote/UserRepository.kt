package com.damiens.mjoba.Data.remote

import com.damiens.mjoba.Model.GeoPoint
import com.damiens.mjoba.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        // Listen for auth state changes
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                _currentUser.value = null
            } else {
                // Load user data from Firestore when auth state changes
                loadCurrentUserData()
            }
        }
    }

    private fun loadCurrentUserData() {
        auth.currentUser?.let { firebaseUser ->
            usersCollection.document(firebaseUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        _currentUser.value = user
                    } else {
                        // User document doesn't exist yet
                        createUserDocument(firebaseUser)
                    }
                }
        }
    }

    private fun createUserDocument(firebaseUser: FirebaseUser) {
        val newUser = User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName ?: "",
            email = firebaseUser.email ?: "",
            phone = firebaseUser.phoneNumber ?: "",
            profileImageUrl = firebaseUser.photoUrl?.toString() ?: "",
            dateCreated = System.currentTimeMillis()
        )

        usersCollection.document(firebaseUser.uid)
            .set(newUser)
            .addOnSuccessListener {
                _currentUser.value = newUser
            }
    }

    // Register a new user with email and password
    suspend fun registerUser(email: String, password: String, name: String, phone: String): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Failed to create user")

            val user = User(
                id = firebaseUser.uid,
                name = name,
                email = email,
                phone = phone,
                dateCreated = System.currentTimeMillis()
            )

            // Save the user data to Firestore
            usersCollection.document(firebaseUser.uid).set(user).await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Login with email and password
    suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Failed to sign in")

            // Fetch user data from Firestore
            val document = usersCollection.document(firebaseUser.uid).get().await()
            val user = document.toObject(User::class.java)
                ?: throw Exception("User data not found")

            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Logout user
    fun logout() {
        auth.signOut()
        _currentUser.value = null
    }

    // Send password reset email
    suspend fun sendPasswordResetEmail(email: String): Result<Boolean> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update user profile
    suspend fun updateUserProfile(
        name: String? = null,
        email: String? = null,
        phone: String? = null,
        address: String? = null,
        location: GeoPoint? = null,
        profileImageUrl: String? = null,
        isServiceProvider: Boolean? = null
    ): Result<User> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))
        val userRef = usersCollection.document(userId)

        return try {
            // Get current user data
            val document = userRef.get().await()
            val currentUser = document.toObject(User::class.java)
                ?: return Result.failure(Exception("User data not found"))

            // Create updated user object
            val updatedUser = currentUser.copy(
                name = name ?: currentUser.name,
                email = email ?: currentUser.email,
                phone = phone ?: currentUser.phone,
                address = address ?: currentUser.address,
                location = location ?: currentUser.location,
                profileImageUrl = profileImageUrl ?: currentUser.profileImageUrl,
                isServiceProvider = isServiceProvider ?: currentUser.isServiceProvider
            )

            // Update in Firestore
            userRef.set(updatedUser).await()
            _currentUser.value = updatedUser

            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get user by ID
    suspend fun getUserById(userId: String): Result<User> {
        return try {
            val document = usersCollection.document(userId).get().await()
            val user = document.toObject(User::class.java)
                ?: throw Exception("User not found")

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Check if user is logged in
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Get current user ID
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}

