package com.example.nayanepal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface MarketplaceDao {
    @Query("SELECT * FROM marketplace_items")
    suspend fun getAllItems(): List<MarketplaceEntity>
    @Query("SELECT * FROM marketplace_items WHERE isRecommended = 1")
    suspend fun getRecommendedItems(): List<MarketplaceEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItems(items: List<MarketplaceEntity>)
}
