package com.shinhyeong.carcompare.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shinhyeong.carcompare.data.local.db.HyundaiVehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HyundaiVehicleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rows: List<HyundaiVehicleEntity>)

    @Query("SELECT COUNT(*) FROM hyundai_vehicles")
    suspend fun count(): Int

    @Query("""
        SELECT * FROM hyundai_vehicles
        WHERE category = :category
        ORDER BY model ASC, COALESCE(variant, '') ASC
    """)
    fun observeByCategory(category: String): Flow<List<HyundaiVehicleEntity>>

    @Query("SELECT * FROM hyundai_vehicles WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<HyundaiVehicleEntity?>
}
