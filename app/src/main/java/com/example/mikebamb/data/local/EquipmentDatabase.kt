package com.example.mikebamb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [EquipmentEntity::class], version = 1, exportSchema = false)
abstract class EquipmentDatabase : RoomDatabase() {

    abstract fun equipmentDao(): EquipmentDao
}