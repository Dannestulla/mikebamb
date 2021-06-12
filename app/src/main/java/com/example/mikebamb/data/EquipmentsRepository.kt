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

    suspend fun addNewItemLocal(newItem: EquipmentEntity) {
        localDB.addNewItemLocal(newItem)
    }


    fun getEquipmentsFromDatabase(): List<EquipmentEntity> {
        return localDB.getEquipmentsFromDatabase()
    }

    suspend fun getEquipmentByPartNumber(partNumber: String): EquipmentEntity {
        return localDB.getEquipmentByPartNumber(partNumber)
    }

    suspend fun removeItem(partNumber: String) {
        localDB.deleteEquipment(partNumber)
    }

    fun getEquipmentByQRCode(qrCode : String) : EquipmentEntity {
        return localDB.getEquipmentByQRcode(qrCode)
    }

    fun printAllQrCodes(): List<String> {
        return localDB.printAllQrCodes()
    }

    fun getMainCategory() : List<String> {
        return localDB.getMainCategory()
    }

    fun getSubCategory(subCategory: String): List<String> {
        return localDB.getSubCategory(subCategory)
    }

    fun getSubSubCategory(subSubCategory: String) : List<String>{
        return localDB.getSubSubCategory(subSubCategory)
    }

    fun getLocalTimestamp(): List<String> {
        return localDB.getTimestamp()
    }

    // REMOTE

    fun initializeRemoteDatabase() {
        return remoteDB.initializeRemoteDatabase()
    }

    fun getAllRemoteData()  {
        return remoteDB.getAllRemoteData()
    }

    fun addNewItemRemote(toEquipmentEntity: EquipmentEntity) {
        remoteDB.addNewItemRemote(toEquipmentEntity)
    }

    suspend fun doesEquipExists(partNumber: String): Boolean {
        return localDB.doesEquipExists(partNumber)

    }
}
