package com.example.nayanepal.data.remote

import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
/**
 * Enterprise Remote Logger.
 * Routes high-volume system events and logs to the cloud NoSQL database via API Gateway.
 */
class RemoteLogger(private val functions: FirebaseFunctions) {
    suspend fun logEvent(type: String, metadata: Map<String, Any>) {
        try {
            val data = hashMapOf(
                "type" to type,
                "metadata" to metadata,
                "timestamp" to System.currentTimeMillis()
            )
            functions.getHttpsCallable("logger-logEvent").call(data).await()
        } catch (e: Exception) {
            // Silently fail local logging to avoid disrupting user experience
        }
    }
}
