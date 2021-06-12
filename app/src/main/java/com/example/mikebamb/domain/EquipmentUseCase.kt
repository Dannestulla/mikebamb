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
}
