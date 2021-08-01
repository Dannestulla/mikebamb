package com.gohan.mikebamb.main_app.domain

import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.Log
import com.gohan.mikebamb.main_app.data.EquipmentsRepository
import com.gohan.mikebamb.main_app.data.local.EquipmentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

class EquipmentUseCase @Inject constructor(
    private val repository: EquipmentsRepository
) {
    private val qrCode = QrCode()

    fun remoteInitializeDatabase(shipId: String) {
        repository.remoteInitializeDatabase(shipId)
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

    suspend fun localGetCaregory3(subSubCategory: String): List<String> {
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

    fun localDeleteAllData() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.localDeleteAllData()
        }
    }

    suspend fun compareRemoteAndLocalData(remoteDBdata: MutableCollection<Any>) {
        val arrayPartNumber = ArrayList<String>()
        val arrayTimestamp = ArrayList<String>()
        var remoteEntity = ArrayList<Any>()
        for (items in remoteDBdata) {
            val splitArray = items.toString().split(",")
            arrayPartNumber.add(splitArray[16].trim())
            arrayTimestamp.add(splitArray[18].trim())
        }
        for (each in remoteDBdata) {
            remoteEntity.add(each)
        }

        var localEquipmentEntity: EquipmentEntity
        var i = 0
        for (items in remoteDBdata) {
            if (localDoesEquipExists(arrayPartNumber[i])) {
                localEquipmentEntity = localGetEquipmentByPartNumber(arrayPartNumber[i])
                val comparison =
                    localEquipmentEntity.timestampEntity.compareTo(arrayTimestamp[i])
                if (comparison < 0) {
                    // Overwriting local database
                    repository.localDeleteEquipment(localEquipmentEntity.partNumber)
                    localAddNewItem(mapNewItem(items))
                    Log.e(
                        "compareRemoteAndLocalData",
                        "Local Overwriting. Item: ${localEquipmentEntity.partNumber}"
                    )
                }
                if (comparison > 0) {
                    // Writing in remote database
                    repository.remoteAddNewItem(localEquipmentEntity)
                    Log.e(
                        "compareRemoteAndLocalData",
                        "Remote Writing. Item: ${localEquipmentEntity.partNumber}"
                    )
                }
            } else {
                //Item not found in localDB, convert from remote to add locally
                localAddNewItem(mapNewItem(items))
                Log.e("compareRemoteAndLocalData", "Local Adding new Equip. Item: $items")
            }
            i++
        }

    }


    private fun mapNewItem(items: Any): EquipmentEntity {
        val newArray =
            items.toString().replace("[", "").replace("]", "").split(",")
        return EquipmentEntity(
            newArray[16].trim(),
            newArray[4].trim(),
            newArray[10].trim(),
            newArray[9].trim(),
            newArray[8].trim(),
            newArray[5].trim(),
            newArray[7].trim(),
            newArray[6].trim(),
            newArray[17].trim(),
            newArray[3].trim(),
            newArray[0].trim(),
            newArray[1].trim(),
            newArray[2].trim(),
            newArray[11].trim(),
            newArray[12].trim(),
            newArray[13].trim(),
            newArray[14].trim(),
            newArray[15].trim(),
            newArray[18].trim()
        )
    }

    fun writeOfflineItemsInCache() {
        CoroutineScope(IO).launch {
            for (item in repository.localGetAllEquipments()) {
                repository.remoteAddNewItem(item)
            }
        }
    }
}

