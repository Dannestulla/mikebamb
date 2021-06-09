package com.example.mikebamb.data

import com.example.mikebamb.data.local.EquipmentDao
import com.example.mikebamb.data.local.EquipmentDatabase
import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.data.remote.CloudFirestore
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class EquipmentsRepository @Inject constructor(
    localDB: EquipmentDatabase,

    ) {
    // LOCAL
    private val localDB = localDB.equipmentDao()


    suspend fun addNewItem(newItem: EquipmentEntity) {
        localDB.addNewItem(newItem)
    }

    fun getListFromDatabase(): List<EquipmentEntity> {
        return localDB.getListFromDB()
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

    fun getLocalTimestamp(): List<String> {
        return localDB.getTimestamp()
    }

    // REMOTE

    fun initializeRemoteDatabase() {
        return CloudFirestore().initializeRemoteDatabase()
    }

}
