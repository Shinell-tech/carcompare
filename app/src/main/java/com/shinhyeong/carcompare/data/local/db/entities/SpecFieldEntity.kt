// app/src/main/java/com/shinhyeong/carcompare/data/local/db/entities/SpecFieldEntity.kt
package com.shinhyeong.carcompare.data.local.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.shinhyeong.carcompare.data.local.db.FieldType

@Entity(
    tableName = "spec_field",
    indices = [Index(value = ["key"], unique = true), Index("groupKey")]
)
data class SpecFieldEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val key: String,          // e.g., power_hp
    val title: String,        // UI label
    val groupKey: String,     // e.g., POWERTRAIN
    val type: FieldType,
    val unit: String? = null,
    val description: String? = null
)