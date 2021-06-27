package com.example.mikebamb.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mikebamb.data.local.EquipmentEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class CloudFirestore @Inject constructor(
) {
    companion object {
        var documentsLiveData = MutableLiveData<MutableCollection<Any>>()
    }

    var remoteDatabase = FirebaseFirestore.getInstance()

    fun remoteInititalizeDatabase() {
        remoteDatabase = FirebaseFirestore.getInstance()
    }

    fun remoteGetAllData() {
        CoroutineScope(IO).launch {
            remoteDatabase.collection("Vessel")
                .get()
                .addOnSuccessListener { task ->
                    val documentCollection = mutableListOf<Any>()
                    for (document in task) {
                        val result = document.data.values.toString().trim()
                        result.replace("\\s".toRegex(), "")
                        documentCollection.add(result.trim())
                    }
                    Log.e("getAllRemoteData", documentCollection.toString())
                    documentsLiveData.postValue(documentCollection)
                }
                .addOnFailureListener { ex -> Log.e("TAG", "Error getting documents: $ex") }
        }
    }

    fun remoteAddNewItem(toEquipmentEntity: EquipmentEntity) {
        val docName = toEquipmentEntity.partNumber
        remoteDatabase.collection("Vessel").document(docName).set(toEquipmentEntity)
            .addOnSuccessListener { Log.e("item added", "DocName: $docName") }
            .addOnFailureListener { ex -> Log.e("TAG", "Error saving documents: $ex") }
    }

    fun remoteDeleteEquipment(partNumber: String) {
        remoteDatabase.collection("Vessel").document(partNumber)
            .delete()
            .addOnSuccessListener { Log.e("item deleted", "Doc Name: $partNumber") }
            .addOnFailureListener { ex -> Log.e("item deleted", "Operation Failed: $ex") }
    }
}
