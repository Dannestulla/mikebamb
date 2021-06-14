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

    fun initializeRemoteDatabase() {
        remoteDatabase = FirebaseFirestore.getInstance()
    }

    fun getAllRemoteData() {
        CoroutineScope(IO).launch {
            remoteDatabase.collection("Vessel")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val documentCollection = mutableListOf<Any>()
                        for (document in task.result!!) {
                            val result = document.data.values.toString()
                            documentCollection.add(result)
                        }
                        Log.e("getAllRemoteData", documentCollection.toString())
                        documentsLiveData.postValue(documentCollection)
                    } else {
                        Log.e("TAG", "Error getting documents.", task.exception)
                    }
                }
        }
    }

    fun addNewItemRemote(toEquipmentEntity: EquipmentEntity) {
        val docName = toEquipmentEntity.partNumber
        remoteDatabase.collection("Vessel").document(docName).set(toEquipmentEntity)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("item added", "DocName: $docName")
                } else {
                    Log.e("TAG", "Error saving documents.", task.exception)
                }
            }
    }
}
