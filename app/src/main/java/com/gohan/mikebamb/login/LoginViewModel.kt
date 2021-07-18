package com.gohan.mikebamb.login

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.ADMIN_LIST
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.EMAIL
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.PASSWORD
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.SHARED_PREF
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.SHIP_ID
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val app: Application
) : ViewModel() {
    var loginOK = MutableLiveData(false)
    lateinit var auth: FirebaseAuth
    var toastReceiver = MutableLiveData<String>()
    var remoteDatabase = FirebaseFirestore.getInstance()

    fun createAccount(email: String, password: String, confpassword: String) {
        if (validadeRegistration(email, password, confpassword)) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toastReceiver.postValue("Registration Complete")
                    sendEmailVerification()
                    //loginOK.postValue("registerToCuisine")
                } else {
                    val ex = task.exception.toString()
                    toastReceiver.postValue("Create Account Failed, $ex")
                }
            }
        } else {
            Toast.makeText(
                app,
                "Email and password must be over 4 letters and passwords must match",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun signIn(email: String, password: String, shipId: String) {
        if (validadeRegistration(email, password, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveSharedPref(email, password, shipId)
                        checkIfUserOrAdmin(email)
                        remoteCheckShipId(shipId)
                    } else {
                        val ex = task.exception.toString()
                        toastReceiver.postValue("SignIn Failed : $ex")
                    }
                }
        }
    }

    private fun checkIfUserOrAdmin(email: String) {
        USER = !ADMIN_LIST.contains(email)
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener {
                Toast.makeText(app, "Confirm this account in your email", Toast.LENGTH_LONG).show()
            }
    }

    fun passwordReset(email: String) {
        if (email.isEmpty()) {
            Toast.makeText(app, "Email field is empty", Toast.LENGTH_LONG).show()
        } else {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful || task.isComplete) {
                        Toast.makeText(
                            app,
                            "Reset Instructions sent to your Email",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(app, "Password Reset Failed", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    fun loadSavedPref(query: String): String {
        val sharedPref = app.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        var answer = ""
        when (query) {
            "Email" -> answer = sharedPref.getString(EMAIL, "").toString()
            "Password" -> answer = sharedPref.getString(PASSWORD, "").toString()
        }
        return answer
    }

    private fun saveSharedPref(email: String, password: String, shipId: String) {
        val sharedPref = app.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        sharedPref.edit().putString(EMAIL, email).apply()
        sharedPref.edit().putString(PASSWORD, password).apply()
        sharedPref.edit().putString(SHIP_ID, shipId).apply()
    }

    private fun remoteCheckShipId(shipId: String) {
        remoteDatabase.collection(shipId).get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    loginOK.postValue(true)
                    toastReceiver.postValue("Login Successfull")
                } else {
                    toastReceiver.postValue("ShipId $shipId Not Found!")
                }
            }
            .addOnFailureListener {
                toastReceiver.postValue("Error fetching ShipID: $it")
            }
    }

    private fun validadeRegistration(
        validEmail: String,
        validPassword: String,
        validConfirmedPassword: String
    ): Boolean {
        if (validEmail.isEmpty() || validPassword.isEmpty() || validConfirmedPassword.isEmpty()) return false
        if (validPassword.length <= 4) return false
        if (validPassword != validConfirmedPassword) return false
        return true
    }

    fun registerNewShip(newShipId : String) {
        remoteDatabase.collection(newShipId).get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    toastReceiver.postValue("ShipID Already Exists")
                } else {
                    toastReceiver.postValue("ShipID Created Successfully")
                    remoteDatabase.collection(newShipId)
                }
            }
            .addOnFailureListener {
                toastReceiver.postValue("Error fetching ShipID: $it")
            }
    }
}


