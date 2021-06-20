package com.example.mikebamb.domain

import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.Log
import com.example.mikebamb.data.EquipmentsRepository
import com.example.mikebamb.data.local.EquipmentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

class EquipmentUseCase @Inject constructor(
    private val repository: EquipmentsRepository
) {
    val qrCode = QrCode()
    fun remoteInitializeDatabase() {
        repository.remoteInitializeDatabase()
    }

    fun createQR(text: String): Bitmap {
        return qrCode.createQR(text)
    }

    fun createQrImageFile(wrapper: ContextWrapper, qrCreated: Bitmap): File {
        return qrCode.createQrImageFile(wrapper, qrCreated)
    }

    fun localPrintAllQrCodes(): List<String> {
        return repository.localPrintAllQrCodes()
    }

    fun localGetSubCategory(subCategory: String): List<String> {
        return repository.localGetSubCategory(subCategory)
    }

    suspend fun localGetSubSubCategory(subSubCategory: String):  List<String>{
        return repository.localGetSubSubCategory(subSubCategory)
    }
    suspend fun localGetSubSubCategoryList(subSubCategory: String): List<EquipmentEntity> {
        return repository.localGetSubSubCategoryList(subSubCategory)
    }

    fun localGetMainCategory(): List<String> {
        return repository.localGetMainCategory()
    }

    suspend fun localGetEquipmentByPartNumber(partNumber: String): EquipmentEntity {
        return repository.localGetEquipmentByPartNumber(partNumber)
    }

    fun localGetAllEquipments(): List<EquipmentEntity> {
        return repository.localGetAllEquipments()
    }

    fun remoteGetAllData() {
        repository.remoteGetAllData()
    }

    suspend fun localDoesEquipExists(partNumber: String): Boolean {
        return repository.localDoesEquipExists(partNumber)
    }

    private suspend fun localAddNewItem(newItem: EquipmentEntity) {
        repository.localAddNewItem(newItem)
    }

    fun compareRemoteAndLocalData(remoteDBdata: MutableCollection<Any>) {
        val arrayPartNumber = ArrayList<String>()
        val arrayTimestamp = ArrayList<String>()
        for (items in remoteDBdata) {
            val splitArray = items.toString().split(",")
            arrayPartNumber.add(splitArray[15].trim())
            arrayTimestamp.add(splitArray[7].trim())
        }
        Log.e("arrayPartNumber", arrayPartNumber.toString())
        Log.e("arrayTimestamp", arrayTimestamp.toString())
        CoroutineScope(Dispatchers.IO).launch {
            var equipmentEntityPartNumber: EquipmentEntity
            var i =0
            for (item in arrayPartNumber) {
                val existsInDB = localDoesEquipExists(item)
                if (existsInDB) {
                    equipmentEntityPartNumber = localGetEquipmentByPartNumber(item)
                    val comparison = equipmentEntityPartNumber.timestampEntity.compareTo(arrayTimestamp[i])
                    if (comparison < 0) {
                        Log.e(
                            "overwriting local database",
                            "equipment from DB: " + equipmentEntityPartNumber.timestampEntity + "Equipment from Remote: " + remoteDBdata
                        )
                    } else if (comparison > 0) {
                        Log.e(
                            "not overwriting",
                            "equipment from DB: " + equipmentEntityPartNumber.timestampEntity + "Equipment from Remote: " + item
                        )
                    }
                } else {
                    for (items in remoteDBdata) {
                        val match = items.toString().trim().contains(item)
                        if (match) {
                            val newArray =
                                items.toString().replace("[", "").replace("]", "").split(",")
                            val newResult = EquipmentEntity(
                                newArray[15].trim(),
                                newArray[0].trim(),
                                newArray[4].trim(),
                                newArray[9].trim(),
                                newArray[16].trim(),
                                newArray[9].trim(),
                                newArray[13].trim(),
                                newArray[8].trim(),
                                newArray[3].trim(),
                                newArray[5].trim(),
                                newArray[6].trim(),
                                newArray[0].trim(),
                                newArray[2].trim(),
                                newArray[18].trim(),
                                newArray[14].trim(),
                                newArray[10].trim(),
                                newArray[11].trim(),
                                newArray[17].trim(),
                                newArray[7].trim()
                            )
                            Log.e("newResult", newArray.toString())
                            localAddNewItem(newResult)
                        }
                    }
                }
                i++
            }
        }
    }
}

