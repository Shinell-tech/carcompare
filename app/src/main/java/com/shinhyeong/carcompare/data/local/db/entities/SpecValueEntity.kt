// app/src/main/java/com/shinhyeong/carcompare/data/local/db/entities/SpecValueEntity.kt
package com.shinhyeong.carcompare.data.local.db.entities

import androidx.room.*
import com.shinhyeong.carcompare.data.local.db.entities.SpecFieldEntity
import com.shinhyeong.carcompare.data.local.db.entities.TrimEntity

@Entity(
    tableName = "spec_value",
    primaryKeys = ["trimId","fieldId"],
    foreignKeys = [
        ForeignKey(
            entity = TrimEntity::class,
            parentColumns = ["id"],
            childColumns = ["trimId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SpecFieldEntity::class,
            parentColumns = ["id"],
            childColumns = ["fieldId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("fieldId"), Index("trimId")]
)
data class SpecValueEntity(
    val trimId: Long,
    val fieldId: Long,
    val numberValue: Double? = null,
    val intValue: Long? = null,
    val textValue: String? = null,
    val boolValue: Boolean? = null,
    val enumKey: String? = null
)
