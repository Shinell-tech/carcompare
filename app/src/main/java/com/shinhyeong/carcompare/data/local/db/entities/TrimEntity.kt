// app/src/main/java/com/shinhyeong/carcompare/data/local/db/entities/TrimEntity.kt
package com.shinhyeong.carcompare.data.local.db.entities

import androidx.room.*

@Entity(
    tableName = "trim",
    foreignKeys = [
        ForeignKey(
            entity = ModelEntity::class,
            parentColumns = ["id"],
            childColumns = ["modelId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("modelId"),
        Index(value = ["modelId","trimName","yearStart"], unique = true),
        Index(value = ["modelId","yearStart","yearEnd"])
    ]
)
data class TrimEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val modelId: Long,
    val trimName: String,
    val yearStart: Int,
    val yearEnd: Int? = null,
    val priceMsrp: Int? = null,
    val currency: String = "KRW"
)