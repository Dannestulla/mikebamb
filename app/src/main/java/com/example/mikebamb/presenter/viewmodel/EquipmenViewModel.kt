package com.example.mikebamb.presenter.viewmodel

import android.app.Application
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mikebamb.data.EquipmentsRepository
import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.domain.EquipmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList


@HiltViewModel
class EquipmenViewModel @Inject constructor(
    val app : Application,
    val repository: EquipmentsRepository
) : AndroidViewModel(app) {
    var qrCodeFromScaner = ""
    lateinit var partNumberClicked: String
    var recyclerViewItems = MutableLiveData<List<EquipmentEntity>>()
    var equipmentDescriptionLiveData = MutableLiveData(EquipmentEntity("","","","","","","","","","","","","","","",""))
    lateinit var equipmentDescriptionData: EquipmentEntity
    var listFromDB = ArrayList<EquipmentEntity>()
    var mAdapter = EquipmentAdapter()
    var emptyEquipmentEntity = EquipmentEntity("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
    var equipmentUseCase = EquipmentUseCase(repository)

    fun addNewItem(newItem: EquipmentEntity) {
        CoroutineScope(IO).launch {
            repository.addNewItem(newItem)
        }
    }

    fun getListFromDatabase() {
        CoroutineScope(IO).launch {
            listFromDB = repository.getListFromDatabase() as ArrayList<EquipmentEntity>
            recyclerViewItems.postValue(listFromDB)
            mAdapter = EquipmentAdapter()
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
        return equipmentUseCase.createQR(text)
    }

    fun getEquipmentByQrCode(qrCodeFromScaner: String) : EquipmentEntity {
        return repository.getEquipmentByQRCode(qrCodeFromScaner)
    }

    fun createQrImageFile(wrapper : ContextWrapper, qrCreated : Bitmap): Uri? {
        val file = equipmentUseCase.createQrImageFile(wrapper, qrCreated)
        val imageUri = FileProvider.getUriForFile(
            app.applicationContext,
            "com.example.mikebamb.fileprovider",
            file
        )
        return imageUri
    }
    fun shareQrCode(image_uri: Uri?, equipmentQRcode: String): Intent {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "application/image"
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Qr Code for $equipmentQRcode")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "New Qr Code for $equipmentQRcode")
        emailIntent.putExtra(Intent.EXTRA_STREAM, image_uri)
        emailIntent.type = "*/*"
        return emailIntent
    }

    fun printAllQrCodes() {
        val allQrCodes = equipmentUseCase.printAllQrCodes()

    }
}