package com.example.mikebamb.data

import com.example.mikebamb.data.local.EquipmentDao
import com.example.mikebamb.data.local.EquipmentDatabase
import com.example.mikebamb.data.local.EquipmentEntity
import javax.inject.Inject

class EquipmentsRepository @Inject constructor(
    db: EquipmentDatabase,
){

    private val database: EquipmentDao = db.equipmentDao()

    suspend fun addNewItem(newItem: EquipmentEntity) {
        database.addNewItem(newItem)
    }


    fun getListFromDatabase() : List<EquipmentEntity> {
        return database.getListFromDB()
    }
}
