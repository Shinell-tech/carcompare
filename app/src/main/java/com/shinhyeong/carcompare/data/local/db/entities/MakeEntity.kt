// app/src/main/java/com/shinhyeong/carcompare/data/local/db/entities/MakeEntity.kt
package com.shinhyeong.carcompare.data.local.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "make",
    indices = [Index(value = ["name"], unique = true)]
)
data class MakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val country: String? = null
)