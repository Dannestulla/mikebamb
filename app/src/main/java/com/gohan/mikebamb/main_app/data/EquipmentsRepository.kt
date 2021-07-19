package com.gohan.mikebamb.main_app.data

import com.gohan.mikebamb.main_app.data.local.EquipmentDatabase
import com.gohan.mikebamb.main_app.data.local.EquipmentEntity
import com.gohan.mikebamb.main_app.data.remote.CloudFirestore
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

    fun localGetCategory1() : List<String> {
        return localDB.localGetCategory1()
    }

    fun localGetCategory2(subCategory: String): List<String> {
        return localDB.localGetCategory2(subCategory)
    }

    suspend fun localGetCaregory3(subSubCategory: String)  : List<String> {
        return localDB.localGetCaregory3(subSubCategory)
    }
    suspend fun localGetCaregory3items(subSubCategory: String) : List<EquipmentEntity> {
        return localDB.localGetCaregory3items(subSubCategory)
    }

    suspend fun localDoesEquipExists(partNumber: String): Boolean {
        return localDB.localDoesEquipExists(partNumber)
    }

    suspend fun localDeleteAllData() {
        localDB.localDeleteAllEquipment()
    }

    // REMOTE

    fun remoteInitializeDatabase(shipId: String) {
        return remoteDB.remoteInititalizeDatabase(shipId)
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
