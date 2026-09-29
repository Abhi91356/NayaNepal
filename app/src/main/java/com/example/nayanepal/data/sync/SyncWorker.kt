package com.example.nayanepal.data.sync

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.nayanepal.data.local.AppDatabase
import com.example.nayanepal.data.model.VerificationDocument
import com.example.nayanepal.data.remote.FirebaseDataSource
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import java.io.File

/**
 * Enterprise Sync Engine.
 */
class SyncWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.getInstance(applicationContext)
        val unsyncedDocs = database.verificationDocumentDao().getUnsyncedDocuments()

        if (unsyncedDocs.isEmpty()) return Result.success()

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val analytics = FirebaseAnalytics.getInstance(applicationContext)
        val functions = FirebaseFunctions.getInstance()
        
        val dataSource = FirebaseDataSource(auth, firestore, storage, analytics, functions)

        var allSuccess = true

        for (entity in unsyncedDocs) {
            try {
                val file = File(entity.storagePath)
                if (!file.exists()) continue
                
                val uri = Uri.fromFile(file)
                val remotePath = dataSource.uploadDocumentImage(entity.userId, entity.documentType, uri)
                
                if (remotePath != null) {
                    val doc = VerificationDocument(
                        id = "",
                        userId = entity.userId,
                        documentType = entity.documentType,
                        storagePath = remotePath,
                        status = "pending"
                    )
                    
                    val created = dataSource.createVerificationDocument(doc)
                    if (created) {
                        database.verificationDocumentDao().upsertDocument(
                            entity.copy(isSynced = true, storagePath = remotePath)
                        )
                        val user = dataSource.getUserProfile(entity.userId)
                        user?.let {
                            dataSource.saveUserProfile(it.copy(kycStatus = "pending"))
                        }
                        file.delete()
                    } else {
                        allSuccess = false
                    }
                } else {
                    allSuccess = false
                }
            } catch (e: Exception) {
                allSuccess = false
            }
        }

        return if (allSuccess) Result.success() else Result.retry()
    }
}
