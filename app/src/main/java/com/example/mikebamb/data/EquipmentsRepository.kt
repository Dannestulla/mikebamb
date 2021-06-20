package com.example.mikebamb.data

import com.example.mikebamb.data.local.EquipmentDatabase
import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.data.remote.CloudFirestore
import javax.inject.Inject

class EquipmentsRepository @Inject constructor(
    localDB: EquipmentDatabase,
    ) {
    // LOCAL
    private val localDB = localDB.equipmentDao()
    private val remoteDB = CloudFirestore()

    suspend fun localAddNewItem(newItem: EquipmentEntity) {
        localDB.localAddNewItem(newItem)
    }

    fun localGetAllEquipments(): List<EquipmentEntity> {
        return localDB.localGetAllEquipments()
    }

    suspend fun localGetEquipmentByPartNumber(partNumber: String): EquipmentEntity {
        return localDB.localGetEquipmentByPartNumber(partNumber)
    }

    suspend fun localDeleteEquipment(partNumber: String) {
        localDB.localDeleteEquipment(partNumber)
    }

    fun localGetEquipmentByQRCode(qrCode : String) : EquipmentEntity {
        return localDB.localGetEquipmentByQRCode(qrCode)
    }

    fun localPrintAllQrCodes(): List<String> {
        return localDB.localPrintAllQrCodes()
    }

    fun localGetMainCategory() : List<String> {
        return localDB.localGetMainCategory()
    }

    fun localGetSubCategory(subCategory: String): List<String> {
        return localDB.localGetSubCategory(subCategory)
    }

    suspend fun localGetSubSubCategory(subSubCategory: String)  : List<String> {
        return localDB.localGetSubSubCategory(subSubCategory)
    }
    suspend fun localGetSubSubCategoryList(subSubCategory: String) : List<EquipmentEntity> {
        return localDB.localGetSubSubCategoryList(subSubCategory)
    }

    suspend fun localDoesEquipExists(partNumber: String): Boolean {
        return localDB.localDoesEquipExists(partNumber)
    }

    // REMOTE

    fun remoteInitializeDatabase() {
        return remoteDB.remoteInititalizeDatabase()
    }

    fun remoteGetAllData()  {
        return remoteDB.remoteGetAllData()
    }

    fun remoteAddNewItem(toEquipmentEntity: EquipmentEntity) {
        remoteDB.remoteAddNewItem(toEquipmentEntity)
    }

    fun remoteDeleteEquipment(partNumber: String) {
        remoteDB.remoteDeleteEquipment(partNumber)
    }
}
