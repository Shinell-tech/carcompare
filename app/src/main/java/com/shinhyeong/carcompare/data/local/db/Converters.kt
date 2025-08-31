// app/src/main/java/com/shinhyeong/carcompare/data/local/db/Converters.kt
package com.shinhyeong.carcompare.data.local.db

import androidx.room.TypeConverter

class Converters {
    @TypeConverter fun fromFieldType(v: FieldType?): String? = v?.name
    @TypeConverter fun toFieldType(v: String?): FieldType? = v?.let(FieldType::valueOf)
}
