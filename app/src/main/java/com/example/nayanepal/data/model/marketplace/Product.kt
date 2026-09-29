package com.example.nayanepal.data.model.marketplace

/**
 * Enterprise Marketplace Product Model.
 * Aligned with robust PostgreSQL schema including stock constraints.
 */
data class Product(
    val id: String = "",
    val sellerId: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val imageUrl: String = "",
    val stockQuantity: Int = 0,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    // Validation logic (Step 2)
    fun isPurchasable(): Boolean = isActive && stockQuantity > 0 && price >= 0
}
