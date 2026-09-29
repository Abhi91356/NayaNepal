package com.example.nayanepal.data.model.wallet

/**
 * Enterprise Transaction Model.
 * Aligned with robust PostgreSQL schema including check constraints and metadata.
 */
data class Transaction(
    val id: String = "",
    val walletId: String = "",
    val amount: Double = 0.0,
    val type: String = "", // credit, debit
    val status: String = "completed", // pending, completed, failed
    val recipientId: String? = null,
    val description: String = "",
    val metadata: Map<String, Any>? = null, // Flexible data (merchant_id, p2p_sender_id)
    val timestamp: Long = System.currentTimeMillis()
) {
    // Validation logic (Step 2)
    fun isDataValid(): Boolean {
        return amount > 0 && (type == "credit" || type == "debit")
    }
}
