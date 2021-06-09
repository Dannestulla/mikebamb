package com.example.mikebamb.data.remote

import android.util.Log
import com.example.mikebamb.data.EquipmentsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class CloudFirestore @Inject constructor(
){
    lateinit var remoteDatabase : FirebaseFirestore

    fun getTimestamp(remoteDB : FirebaseFirestore) {
        remoteDB.collection("last_modified")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        var remoteTimestamp = document.data.values.toString()
                        remoteTimestamp = remoteTimestamp.replace("[", "").replace("]", "")
                        Log.e("TAG", remoteTimestamp)
                    }
                } else {
                    Log.e("TAG", "Error getting documents.", task.exception)
                }
            }
    }

    fun initializeRemoteDatabase() {
        remoteDatabase = FirebaseFirestore.getInstance()

    }

}