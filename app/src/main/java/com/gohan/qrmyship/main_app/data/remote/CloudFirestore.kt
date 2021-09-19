package com.gohan.qrmyship.main_app.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gohan.qrmyship.main_app.data.local.EquipmentEntity
import com.gohan.qrmyship.main_app.domain.myConstants.COLLECTION_NAME
import com.google.firebase.firestore.FirebaseFirestore

class CloudFirestore {

    companion object {
        var documentsLiveData = MutableLiveData<ArrayList<Any>>()
        var canUserEdit = MutableLiveData<Boolean>()
    }

    var remoteDatabase = FirebaseFirestore.getInstance()

    fun remoteInititalizeDatabase(shipId: String) {
        COLLECTION_NAME = shipId
    }

    fun remoteGetAllData() {
        remoteDatabase.collection(COLLECTION_NAME)
            .get()
            .addOnSuccessListener { task ->
                val documentCollection = ArrayList<Any>()
                var i = 0
                for (document in task) {
                    val result = document.data.toSortedMap()
                    documentCollection.add(result.values)
                    i++
                }
                documentsLiveData.postValue(documentCollection)
            }
            .addOnFailureListener { ex -> Log.e("TAG", "Error getting documents: $ex") }
    }

    fun remoteAddNewItem(toEquipmentEntity: EquipmentEntity) {
        val docName = toEquipmentEntity.partNumber
        remoteDatabase.collection(COLLECTION_NAME).document(docName).set(toEquipmentEntity)
            .addOnSuccessListener { Log.e("item added", "DocName: $docName") }
            .addOnFailureListener { ex -> Log.e("TAG", "Error saving documents: $ex") }
    }

    fun remoteDeleteEquipment(partNumber: String) {
        remoteDatabase.collection(COLLECTION_NAME).document(partNumber)
            .delete()
            .addOnSuccessListener { Log.e("item deleted", "Doc Name: $partNumber") }
            .addOnFailureListener { ex -> Log.e("item deleted", "Operation Failed: $ex") }
    }

    fun checkIfUserCanEdit(userLoggedIn: String?, vesselLoggedIn: String?) {
        if (vesselLoggedIn != null) {
            remoteDatabase.collection("VesselInfo").document(vesselLoggedIn).get()
                .addOnSuccessListener { task ->
                    val result = task.data?.keys
                    if (result?.contains(userLoggedIn) == true) {
                        canUserEdit.postValue(true)
                    }
                }
        }
    }
}



