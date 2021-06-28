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

    private suspend fun localAddNewItem(newItem: EquipmentEntity) {
        repository.localAddNewItem(newItem)
    }

    fun localPrintAllQrCodes(): List<String> {
        return repository.localPrintAllQrCodes()
    }
    fun localGetCategory1(): List<String> {
        return repository.localGetCategory1()
    }

    fun localGetCategory2(subCategory: String): List<String> {
        return repository.localGetCategory2(subCategory)
    }

    suspend fun localGetCaregory3(subSubCategory: String):  List<String>{
        return repository.localGetCaregory3(subSubCategory)
    }
    suspend fun localGetCaregory3items(subSubCategory: String): List<EquipmentEntity> {
        return repository.localGetCaregory3items(subSubCategory)
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


    fun compareRemoteAndLocalData(remoteDBdata: MutableCollection<Any>) {
        val arrayPartNumber = ArrayList<String>()
        val arrayTimestamp = ArrayList<String>()
        for (items in remoteDBdata) {
            val splitArray = items.toString().split(",")
            arrayPartNumber.add(splitArray[16].trim())
            arrayTimestamp.add(splitArray[18].trim())
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
                            //newArray.sorted()
                            val newResult = EquipmentEntity(
                                newArray[16],
                                newArray[4],
                                newArray[10],
                                newArray[9],
                                newArray[8],
                                newArray[5],
                                newArray[7],
                                newArray[6],
                                newArray[17],
                                newArray[3],
                                newArray[0],
                                newArray[1],
                                newArray[2],
                                newArray[11],
                                newArray[12],
                                newArray[13],
                                newArray[14],
                                newArray[15],
                                newArray[18]
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

