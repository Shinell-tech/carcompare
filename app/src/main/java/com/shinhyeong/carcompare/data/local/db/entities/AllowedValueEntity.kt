// app/src/main/java/com/shinhyeong/carcompare/data/local/db/entities/AllowedValueEntity.kt
package com.shinhyeong.carcompare.data.local.db.entities

import androidx.room.*

@Entity(
    tableName = "allowed_value",
    foreignKeys = [
        ForeignKey(
            entity = SpecFieldEntity::class,
            parentColumns = ["id"],
            childColumns = ["fieldId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("fieldId"), Index(value = ["fieldId","key"], unique = true)]
)
data class AllowedValueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fieldId: Long,
    val key: String,     // e.g., RWD
    val label: String    // e.g., 후륜
)
