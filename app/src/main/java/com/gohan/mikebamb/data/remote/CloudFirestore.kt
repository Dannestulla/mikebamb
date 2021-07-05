package com.gohan.mikebamb.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gohan.mikebamb.data.local.EquipmentEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class CloudFirestore @Inject constructor(
) {
    companion object {
        var documentsLiveData = MutableLiveData<ArrayList<Any>>()
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
                    val documentCollection = ArrayList<Any>()
                    var i = 0
                    for (document in task) {
                        val result = document.data.toSortedMap()
                        documentCollection.add(result.values)
                        //result.replace("\\s".toRegex(), "")
                        i++
                    }
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
