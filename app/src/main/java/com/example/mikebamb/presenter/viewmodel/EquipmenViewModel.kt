package com.example.mikebamb.presenter.viewmodel

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mikebamb.data.EquipmentsRepository
import com.example.mikebamb.data.local.EquipmentEntity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EquipmenViewModel @Inject constructor(
    val repository: EquipmentsRepository
) : ViewModel() {
    var qrCodeFromScaner = ""
    lateinit var partNumberClicked: String
    var recyclerViewItems = MutableLiveData<List<EquipmentEntity>>()
    var equipmentDescriptionLiveData = MutableLiveData<EquipmentEntity>()
    lateinit var equipmentDescriptionData: EquipmentEntity

    fun addNewItem(newItem: EquipmentEntity) {
        CoroutineScope(IO).launch {
            repository.addNewItem(newItem)
        }
    }



    fun getListFromDatabase() {
        CoroutineScope(IO).launch {
            val listFromDB = repository.getListFromDatabase()
            recyclerViewItems.postValue(listFromDB)
        }
    }

    suspend fun getEquipmentByPartNumber(partNumber: String): EquipmentEntity {
        return repository.getEquipmentByPartNumber(partNumber)
    }

    fun getInDBEquipmentDescription() {
        CoroutineScope(IO).launch {
            equipmentDescriptionData = getEquipmentByPartNumber(
                partNumberClicked
            )
            equipmentDescriptionLiveData.postValue(equipmentDescriptionData)
        }
    }

    suspend fun deleteEquipment(partNumber: String) {
        repository.removeItem(partNumber)
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

    fun getEquipmentByQrCode() {
        repository.getEquipmentByQRCode()
    }
}