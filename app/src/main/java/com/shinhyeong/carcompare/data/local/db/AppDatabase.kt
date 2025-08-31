// app/src/main/java/com/shinhyeong/carcompare/data/local/db/AppDatabase.kt
package com.shinhyeong.carcompare.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shinhyeong.carcompare.data.local.db.dao.CarCompareDao
import com.shinhyeong.carcompare.data.local.db.entities.*

@Database(
    entities = [
        MakeEntity::class,
        ModelEntity::class,
        TrimEntity::class,
        SpecFieldEntity::class,
        AllowedValueEntity::class,
        SpecValueEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carCompareDao(): CarCompareDao
    companion object { const val NAME = "carcompare.db" }
}
