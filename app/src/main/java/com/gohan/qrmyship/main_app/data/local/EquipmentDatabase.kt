package com.gohan.qrmyship.main_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [EquipmentEntity::class], version = 5, exportSchema = false)
abstract class EquipmentDatabase : RoomDatabase() {

    abstract fun equipmentDao(): EquipmentDao
}