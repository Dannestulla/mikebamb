package com.example.mikebamb.domain

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.example.mikebamb.data.EquipmentsRepository
import com.example.mikebamb.data.local.EquipmentEntity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

class EquipmentUseCase @Inject constructor(
    private val repository: EquipmentsRepository
) {

    fun initializeRemoteDatabase() {
        repository.initializeRemoteDatabase()
    }

    fun createQR(text: String): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d("CreateQR", "${e.message}")
        }
        return bitmap
    }

    fun createQrImageFile(wrapper: ContextWrapper, qrCreated: Bitmap): File {
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.png")
        val stream = FileOutputStream(file)
        qrCreated.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
        return file
    }

    fun printAllQrCodes(): List<String> {
        return repository.printAllQrCodes()
    }

    fun getSubCategory(subCategory: String): List<String> {
        return repository.getSubCategory(subCategory)
    }

    fun getSubSubCategory(subSubCategory : String) : List<String> {
        return repository.getSubSubCategory(subSubCategory)
    }

    fun getMainCategory(): List<String> {
        return repository.getMainCategory()
    }

    suspend fun getEquipmentByPartNumber(partNumber: String): EquipmentEntity {
        return repository.getEquipmentByPartNumber(partNumber)
    }

    fun getEquipmentsFromDatabase(): List<EquipmentEntity> {
        return repository.getEquipmentsFromDatabase()
    }

    fun getAllRemoteData() {
        repository.getAllRemoteData()
    }

    suspend fun doesEquipExists(partNumber: String): Boolean {
        return repository.doesEquipExists(partNumber)
    }

    suspend fun addNewItemLocal(newItem : EquipmentEntity) {
        repository.addNewItemLocal(newItem)
    }

    fun compareRemoteAndLocalData(remoteDBdata: MutableCollection<Any>) {
        val arrayPartNumber = java.util.ArrayList<String>()
        val arrayTimestamp = java.util.ArrayList<String>()
        for (items in remoteDBdata) {
            val splitArray = items.toString().split(",")
            arrayPartNumber.add(splitArray[15])
            arrayTimestamp.add(splitArray[7])
        }
        Log.e("arrayPartNumber", arrayPartNumber.toString())
        Log.e("arrayPartNumber", arrayTimestamp.toString())
        CoroutineScope(Dispatchers.IO).launch {
            var equipmentEntityPartNumber: EquipmentEntity
            for (partNumber in arrayPartNumber) {
                val existsInDB = doesEquipExists(partNumber)
                if (existsInDB) {
                    equipmentEntityPartNumber = getEquipmentByPartNumber(partNumber)
                    val comparison = equipmentEntityPartNumber.timestampEntity.compareTo(partNumber)
                    if (comparison < 0) {
                        Log.e(
                            "overwriting local database",
                            "equipment from DB: " + equipmentEntityPartNumber.timestampEntity + "Equipment from Remote: " + partNumber
                        )
                    } else if (comparison > 0){
                        Log.e(
                            "not overwriting",
                            "equipment from DB: " + equipmentEntityPartNumber.timestampEntity + "Equipment from Remote: " + partNumber
                        )
                    }
                } else {
                    for (items in remoteDBdata) {
                        val match = items.toString().contains(partNumber)
                        if (match) {
                            val newArray = items.toString().replace("[","").replace("]","").split(",")
                            val newResult = EquipmentEntity(
                                newArray[15],
                                newArray[0],
                                newArray[4],
                                newArray[9],
                                newArray[16],
                                newArray[9],
                                newArray[13],
                                newArray[8],
                                newArray[3],
                                newArray[5],
                                newArray[6],
                                newArray[0],
                                newArray[2],
                                newArray[18],
                                newArray[14],
                                newArray[10],
                                newArray[11],
                                newArray[17],
                                newArray[7])
                            Log.e("newResult", newArray.toString())
                            addNewItemLocal(newResult)
                        }
                    }
                }
            }
        }
    }
    }

