package com.example.nayanepal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "marketplace_items")
data class MarketplaceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String,
    val stockQuantity: Int,
    val isActive: Boolean = true,
    val isRecommended: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)
