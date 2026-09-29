package com.example.nayanepal.data.remote

import android.net.Uri
import android.os.Bundle
import com.example.nayanepal.core.auth.domain.model.User
import com.example.nayanepal.data.model.VerificationDocument
import com.example.nayanepal.data.model.wallet.Transaction
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Enterprise-grade Data Source following the API Gateway / Microservices pattern.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class FirebaseDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val analytics: FirebaseAnalytics,
    private val functions: FirebaseFunctions
) {

    // --- AUTH & IDENTITY SERVICE ---
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun logEvent(name: String, params: Bundle? = null) {
        analytics.logEvent(name, params)
    }

    // --- USER PROFILE & RBAC SERVICE ---
    suspend fun getUserProfile(uid: String): User? = suspendCancellableCoroutine { cont ->
        try {
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { snap ->
                    val user = try {
                        snap.toObject(User::class.java)?.copy(id = uid)
                    } catch (e: Exception) {
                        null
                    }
                    cont.resume(user) {}
                }
                .addOnFailureListener {
                    // Fallback for profile loading
                    cont.resume(null) {}
                }
        } catch (e: Exception) {
            cont.resume(null) {}
        }
    }

    suspend fun saveUserProfile(user: User): Boolean = try {
        val data = hashMapOf(
            "name" to user.name,
            "email" to user.email,
            "phone" to user.phone,
            "language" to user.language
        )
        // Also try direct Firestore save as backup to Cloud Functions
        try {
            firestore.collection("users").document(user.id).set(user).await()
        } catch (e: Exception) {
            // Log or ignore
        }
        
        try {
            functions.getHttpsCallable("profile-updateProfile").call(data).await()
        } catch (e: Exception) {
            // Function might not be deployed
        }
        true
    } catch (e: Exception) {
        false
    }

    // --- WALLET & PAYMENT SERVICE ---
    suspend fun getWalletBalance(userId: String): Double = try {
        val result = functions.getHttpsCallable("wallet-getBalance")
            .call(mapOf("userId" to userId))
            .await()
        (result.data as? Map<*, *>)?.get("balance") as? Double ?: 0.0
    } catch (e: Exception) {
        0.0
    }

    suspend fun initiatePayment(transaction: Transaction): Boolean = try {
        val data = hashMapOf(
            "amount" to transaction.amount,
            "recipientId" to transaction.recipientId,
            "type" to transaction.type,
            "description" to transaction.description
        )
        functions.getHttpsCallable("wallet-processPayment").call(data).await()
        true
    } catch (e: Exception) {
        false
    }

    // --- VERIFICATION SERVICE ---
    suspend fun uploadDocumentImage(
        userId: String,
        docType: String,
        uri: Uri
    ): String? = suspendCancellableCoroutine { cont ->
        val path = "user_documents/$userId/$docType.jpg"
        val ref = storage.reference.child(path)
        ref.putFile(uri)
            .addOnSuccessListener { cont.resume(path) {} }
            .addOnFailureListener { cont.resume(null) {} }
    }

    suspend fun createVerificationDocument(doc: VerificationDocument): Boolean = try {
        val data = hashMapOf(
            "userId" to doc.userId,
            "documentType" to doc.documentType,
            "storagePath" to doc.storagePath
        )
        functions.getHttpsCallable("verification-submitDocument").call(data).await()
        true
    } catch (e: Exception) {
        false
    }

    // --- AGRICULTURE ADVISORY SERVICE ---
    suspend fun getAgriAdvice(location: String): List<Map<String, Any>> = try {
        val result = functions.getHttpsCallable("agri-getAdvice")
            .call(mapOf("location" to location))
            .await()
        result.data as? List<Map<String, Any>> ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
}
