package com.example.mikebamb.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface EquipmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewItem(newEquip: EquipmentEntity)

    @Query("DELETE FROM equipment WHERE name =:equipment")
    suspend fun deleteEquipment(equipment: String)

    @Query("SELECT * FROM equipment WHERE part_number=:partNumber")
    suspend fun findEquipment(partNumber: String): List<EquipmentEntity>

    @Query("SELECT * FROM equipment")
    fun getListFromDB() : List<EquipmentEntity>

}