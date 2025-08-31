// app/src/main/java/com/shinhyeong/carcompare/data/local/db/entities/ModelEntity.kt
package com.shinhyeong.carcompare.data.local.db.entities

import androidx.room.*

@Entity(
    tableName = "model",
    foreignKeys = [
        ForeignKey(
            entity = MakeEntity::class,
            parentColumns = ["id"],
            childColumns = ["makeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("makeId"), Index(value = ["makeId","name"], unique = true)]
)
data class ModelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val makeId: Long,
    val name: String,
    val bodyStyle: String? = null
)