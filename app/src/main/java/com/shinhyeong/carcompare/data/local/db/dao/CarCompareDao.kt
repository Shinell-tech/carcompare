// app/src/main/java/com/shinhyeong/carcompare/data/local/db/dao/CarCompareDao.kt
package com.shinhyeong.carcompare.data.local.db.dao

import androidx.room.*
import com.shinhyeong.carcompare.data.local.db.entities.*
import com.shinhyeong.carcompare.feature.compare.SpecRow
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.shinhyeong.carcompare.data.local.db.entities.TrimEntity

@Dao
interface CarCompareDao {
    // --- Counts ---
    @Query("SELECT COUNT(*) FROM spec_field")
    suspend fun countSpecFields(): Int

    // --- Inserts (seed/ETL는 REPLACE 사용해 간편 upsert) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecFields(items: List<SpecFieldEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllowedValues(items: List<AllowedValueEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMakes(items: List<MakeEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModels(items: List<ModelEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrims(items: List<TrimEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecValues(items: List<SpecValueEntity>): List<Long>

    // --- Lookups ---
    @Query("SELECT id FROM spec_field WHERE key = :key LIMIT 1")
    suspend fun getFieldIdByKey(key: String): Long?

    @Query("SELECT id FROM make WHERE name = :name LIMIT 1")
    suspend fun getMakeIdByName(name: String): Long?

    @Query("SELECT id FROM model WHERE makeId = :makeId AND name = :name LIMIT 1")
    suspend fun getModelIdByMakeAndName(makeId: Long, name: String): Long?

    @Query("SELECT id FROM trim WHERE modelId = :modelId AND trimName = :trimName LIMIT 1")
    suspend fun getTrimIdByModelAndTrim(modelId: Long, trimName: String): Long?

    // --- Fetch for compare ---
    @Transaction
    @Query("SELECT * FROM trim WHERE id IN (:trimIds)")
    suspend fun getTrims(trimIds: List<Long>): List<TrimEntity>

    @Query("""
        SELECT sf.key, sf.title, sf.groupKey, sf.type, sf.unit,
               sv.numberValue, sv.intValue, sv.textValue, sv.boolValue, sv.enumKey, sv.trimId
        FROM spec_field sf
        JOIN spec_value sv ON sv.fieldId = sf.id
        WHERE sv.trimId IN (:trimIds)
        ORDER BY sf.groupKey, sf.id
    """)
    suspend fun getSpecMatrix(trimIds: List<Long>): List<SpecRow>

    @Query("SELECT * FROM trim ORDER BY id DESC LIMIT :limit")
    suspend fun getRecentTrims(limit: Int): List<TrimEntity>

    @Query("SELECT id FROM trim ORDER BY id LIMIT :limit")
    suspend fun pickTrimIds(limit: Int): List<Long>

}
