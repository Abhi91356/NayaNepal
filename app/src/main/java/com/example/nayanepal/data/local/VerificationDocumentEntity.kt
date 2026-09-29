package com.example.nayanepal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "verification_documents")
data class VerificationDocumentEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val documentType: String,
    val storagePath: String,
    val status: String,
    val rejectionReason: String?,
    val createdAt: Long,
    val isSynced: Boolean
)
