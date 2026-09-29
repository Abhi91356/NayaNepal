package com.example.nayanepal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface VerificationDocumentDao {
    @Query("SELECT * FROM verification_documents WHERE userId = :userId")
    suspend fun getDocumentsForUser(userId: String): List<VerificationDocumentEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDocument(doc: VerificationDocumentEntity)
    @Query("SELECT * FROM verification_documents WHERE isSynced = 0")
    suspend fun getUnsyncedDocuments(): List<VerificationDocumentEntity>
}
