package com.shinhyeong.carcompare.data.local.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "spec_value",
    primaryKeys = ["trimId", "fieldId"],
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
