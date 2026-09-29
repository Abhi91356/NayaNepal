package com.example.nayanepal.core.auth.data.repository

import com.example.nayanepal.core.auth.domain.model.User
import com.example.nayanepal.core.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {
    override suspend fun loginWithEmail(email: String, pass: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                // Fetch additional user data from Firestore with fallback
                val user = try {
                    val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()
                    User(
                        id = firebaseUser.uid,
                        email = firebaseUser.email ?: "",
                        name = userDoc.getString("name") ?: firebaseUser.displayName ?: "User",
                        phone = userDoc.getString("phone") ?: ""
                    )
                } catch (e: Exception) {
                    // Fallback to Auth basic info if Firestore fails (e.g. PERMISSION_DENIED)
                    User(
                        id = firebaseUser.uid,
                        email = firebaseUser.email ?: "",
                        name = firebaseUser.displayName ?: "User",
                        phone = ""
                    )
                }
                Result.success(user)
            } else {
                Result.failure(Exception("Login failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmail(name: String, email: String, phone: String, pass: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                // Set display name in Firebase Auth
                try {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    firebaseUser.updateProfile(profileUpdates).await()
                } catch (e: Exception) {
                    // Non-critical failure
                }

                val user = User(
                    id = firebaseUser.uid,
                    name = name,
                    email = email,
                    phone = phone
                )

                // Attempt to save user profile in Firestore
                try {
                    firestore.collection("users").document(firebaseUser.uid)
                        .set(user)
                        .await()
                } catch (e: Exception) {
                    // If Firestore is disabled/denied, we still count registration as success 
                    // since the Auth account was created.
                }

                Result.success(user)
            } else {
                Result.failure(Exception("Registration failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
