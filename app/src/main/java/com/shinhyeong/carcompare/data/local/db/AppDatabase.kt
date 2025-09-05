package com.shinhyeong.carcompare.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shinhyeong.carcompare.data.local.db.dao.HyundaiVehicleDao

@Database(
    entities = [HyundaiVehicleEntity::class],
    version = 4, // 스키마 바뀌었으니 +1
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hyundaiVehicleDao(): HyundaiVehicleDao
}
