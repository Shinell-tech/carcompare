package com.shinhyeong.carcompare.data.local.seed

import android.content.Context
import androidx.room.withTransaction
import com.shinhyeong.carcompare.data.local.db.AppDatabase
import com.shinhyeong.carcompare.data.local.db.FieldType
import com.shinhyeong.carcompare.data.local.db.entities.AllowedValueEntity
import com.shinhyeong.carcompare.data.local.db.entities.MakeEntity
import com.shinhyeong.carcompare.data.local.db.entities.ModelEntity
import com.shinhyeong.carcompare.data.local.db.entities.SpecFieldEntity
import com.shinhyeong.carcompare.data.local.db.entities.SpecValueEntity
import com.shinhyeong.carcompare.data.local.db.entities.TrimEntity
import com.shinhyeong.carcompare.di.IODispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

@Singleton
class SeedImporter @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val db: AppDatabase,
    private val json: Json,
    @IODispatcher private val io: CoroutineDispatcher
) {
    suspend fun importIfNeeded() = withContext(io) {
        db.withTransaction {
            val count = db.carCompareDao().countSpecFields()
            if (count > 0) return@withTransaction

            val text = appContext.assets.open("cars_seed.json").bufferedReader().use { it.readText() }
            val seed = json.decodeFromString(Seed.serializer(), text)
            val dao = db.carCompareDao()

            // 1) Fields
            val fieldEntities = seed.fields.map {
                SpecFieldEntity(
                    key = it.key,
                    title = it.title,
                    groupKey = it.group,
                    type = FieldType.valueOf(it.type),
                    unit = it.unit,
                    description = it.description
                )
            }
            dao.insertSpecFields(fieldEntities)

            val fieldIdByKey = seed.fields.associate { sf ->
                val id = dao.getFieldIdByKey(sf.key)
                    ?: error("Field not found after insert: ${sf.key}")
                sf.key to id
            }

            // 2) Allowed values
            val allowed = buildList {
                seed.allowedValues.forEach { (fieldKey, list) ->
                    val fid = fieldIdByKey[fieldKey] ?: return@forEach
                    list.forEach { av ->
                        add(AllowedValueEntity(fieldId = fid, key = av.key, label = av.label))
                    }
                }
            }
            if (allowed.isNotEmpty()) dao.insertAllowedValues(allowed)

            // 3) Makes
            val makeEntities = seed.makes.map { MakeEntity(name = it.name, country = it.country) }
            dao.insertMakes(makeEntities)
            val makeIdByName = seed.makes.associate {
                val id = dao.getMakeIdByName(it.name) ?: error("Make insert failed: ${it.name}")
                it.name to id
            }

            // 4) Models
            val modelEntities = seed.models.map {
                val makeId = makeIdByName[it.make] ?: error("Make not found: ${it.make}")
                ModelEntity(makeId = makeId, name = it.name, bodyStyle = it.bodyStyle)
            }
            dao.insertModels(modelEntities)
            val modelIdByRef = seed.models.associate {
                val makeId = makeIdByName[it.make]!!
                val id = dao.getModelIdByMakeAndName(makeId, it.name)
                    ?: error("Model insert failed: ${it.make}:${it.name}")
                "${it.make}:${it.name}" to id
            }

            // 5) Trims
            val trimEntities = seed.trims.map {
                val modelId = modelIdByRef[it.model] ?: error("Model ref not found: ${it.model}")
                TrimEntity(
                    modelId = modelId,
                    trimName = it.trimName,
                    yearStart = it.yearStart,
                    yearEnd = it.yearEnd,
                    priceMsrp = it.priceMsrp,
                    currency = it.currency
                )
            }
            dao.insertTrims(trimEntities)
            val trimIdByRef = seed.trims.associate {
                val modelId = modelIdByRef[it.model]!!
                val id = dao.getTrimIdByModelAndTrim(modelId, it.trimName)
                    ?: error("Trim insert failed: ${it.model}:${it.trimName}")
                "${it.model}:${it.trimName}" to id
            }

            // 6) Values
            val valueEntities = seed.values.map { v ->
                val trimId = trimIdByRef[v.trim] ?: error("Trim ref not found: ${v.trim}")
                val fieldId = fieldIdByKey[v.field] ?: error("Field key not found: ${v.field}")
                SpecValueEntity(
                    trimId = trimId,
                    fieldId = fieldId,
                    numberValue = v.number,
                    intValue = v.int,
                    textValue = v.text,
                    boolValue = v.bool,
                    enumKey = v.enum
                )
            }
            if (valueEntities.isNotEmpty()) dao.insertSpecValues(valueEntities)
        }
    }
}
