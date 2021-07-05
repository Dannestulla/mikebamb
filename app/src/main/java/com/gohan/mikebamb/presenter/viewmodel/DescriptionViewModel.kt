package com.gohan.mikebamb.presenter.viewmodel

import android.app.Application
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gohan.mikebamb.data.EquipmentsRepository
import com.gohan.mikebamb.data.local.EquipmentEntity
import com.gohan.mikebamb.domain.EquipmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DescriptionViewModel @Inject constructor(
    val app: Application,
    val repository: EquipmentsRepository,

    ) : AndroidViewModel(app) {
    var qrCodeFromScaner = ""
    lateinit var partNumberClicked: String
    var equipmentDescriptionLiveData = MutableLiveData(
        EquipmentEntity(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
    )
    lateinit var equipmentDescriptionData: EquipmentEntity
    var emptyEquipmentEntity =
        EquipmentEntity("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
    var equipmentUseCase = EquipmentUseCase(repository)

    fun localAddNewItem(newItem: EquipmentEntity) {
        CoroutineScope(IO).launch {
            repository.localAddNewItem(newItem)
        }
    }


    suspend fun localGetEquipmentByPartNumber(partNumber: String): EquipmentEntity {
        return equipmentUseCase.localGetEquipmentByPartNumber(partNumber)
    }

    fun getInDBEquipmentDescription() {
        CoroutineScope(IO).launch {
            equipmentDescriptionData = localGetEquipmentByPartNumber(
                partNumberClicked
            )
            equipmentDescriptionLiveData.postValue(equipmentDescriptionData)
        }
    }

    suspend fun localDeleteEquipment(partNumber: String) {
        repository.localDeleteEquipment(partNumber)
    }

    fun createQR(text: String): Bitmap {
        return equipmentUseCase.createQR(text)
    }

    fun localGetEquipmentByQRCode(qrCodeFromScaner: String): EquipmentEntity {
        return repository.localGetEquipmentByQRCode(qrCodeFromScaner)
    }

    fun createQrImageFile(wrapper: ContextWrapper, qrCreated: Bitmap): Uri? {
        val file = equipmentUseCase.createQrImageFile(wrapper, qrCreated)
        val imageUri = FileProvider.getUriForFile(
            app.applicationContext,
            "com.gohan.mikebamb.fileprovider",
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

    fun localPrintAllQrCodes() {
        val allQrCodes = equipmentUseCase.localPrintAllQrCodes()
    }

    fun remoteAddNewItem(toEquipmentEntity: EquipmentEntity) {
        repository.remoteAddNewItem(toEquipmentEntity)
    }

    fun remoteDeleteEquipment(partNumber: String) {
        repository.remoteDeleteEquipment(partNumber)

    }
}