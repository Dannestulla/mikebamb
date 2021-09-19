package com.gohan.qrmyship.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gohan.qrmyship.main_app.domain.myConstants.EMAIL
import com.gohan.qrmyship.main_app.domain.myConstants.EMPTY_EQUIPMENT_ENTITY
import com.gohan.qrmyship.main_app.domain.myConstants.NEW_SHIP_ACCOUNT
import com.gohan.qrmyship.main_app.domain.myConstants.PASSWORD
import com.gohan.qrmyship.main_app.domain.myConstants.SHARED_PREF
import com.gohan.qrmyship.main_app.domain.myConstants.VESSEL_EMAIL
import com.gohan.qrmyship.main_app.domain.myConstants.VESSEL_ID
import com.gohan.qrmyship.main_app.domain.myConstants.VESSEL_PASSWORD
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val app: Application
) : ViewModel() {
    var auth = Firebase.auth
    var toastReceiver = MutableLiveData<String>()
    var remoteDatabase = Firebase.firestore
    var shipLoginOk = MutableLiveData(false)
    var loginOK = MutableLiveData(false)
    var loadingBar = MutableLiveData(false)
    private val sharedPref: SharedPreferences =
        app.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    var userEmail = ""

    fun createAccount(email: String, password: String) {
        if (validadeRegistration(email, password)) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toastReceiver.postValue("Registration Complete")
                    sendEmailVerification()

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

    fun signInForUserAndVessel(email: String, password: String) {
        if (validadeRegistration(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (loginOK.value == true) {
                            saveShipEmailAndPassword(email, password)
                            shipLoginOk.postValue(true)
                        }
                        loginOK.postValue(true)

                    } else {
                        val ex = task.exception.toString()
                        toastReceiver.postValue("SignIn Failed : $ex")
                        loginOK.postValue(false)
                    }
                }
                .addOnFailureListener {

                }
        } else {
            toastReceiver.postValue("Must Enter Valid Email and Password")
            loadingBar.postValue(false)
        }
    }

    fun addUserToVesselAccount(vesselEmail: String) {
        val userEmail = hashMapOf(
            userEmail to true,
        )
        remoteDatabase.collection("VesselInfo").document(vesselEmail).set(userEmail, SetOptions.merge())
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
                            "Reset Instructions sent to $email",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(app, "Password Reset Failed", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    fun loadStoredValues(query: String): String {
        val sharedPref = app.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        var answer = ""
        when (query) {
            "email" -> answer = sharedPref.getString(EMAIL, "").toString()
            "password" -> answer = sharedPref.getString(PASSWORD, "").toString()
            "ShipId" -> answer = sharedPref.getString(VESSEL_ID, "").toString()
            "VesselPassword" -> answer = sharedPref.getString(VESSEL_PASSWORD, "").toString()
        }
        return answer
    }

    fun saveEmailAndPassword(email: String, password: String) {
        sharedPref.edit().putString(EMAIL, email).apply()
        sharedPref.edit().putString(PASSWORD, password).apply()
    }

    private fun saveShipEmailAndPassword(email: String, password: String) {
        sharedPref.edit().putString(VESSEL_EMAIL, email).apply()
        sharedPref.edit().putString(VESSEL_PASSWORD, password).apply()
    }

    fun checkShipId(shipId: String) {
        if (shipId.isNotEmpty()) {
            remoteDatabase.collection(shipId).get()
                .addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        compareOldIdWithNewLogin(shipId)
                        saveShipID(shipId)
                    }
                }
                .addOnFailureListener {
                    toastReceiver.postValue("Error fetching ShipID: $it")
                }
        }
    }

    private fun compareOldIdWithNewLogin(shipId: String) {
        //Used to clean local DB in case of login in a new ship account
        val savedId = sharedPref.getString(VESSEL_ID, "")
        if (savedId != shipId) {
            sharedPref.edit().putBoolean(NEW_SHIP_ACCOUNT, true).apply()
        }
    }

    private fun saveShipID(shipId: String) {
        sharedPref.edit().putString(VESSEL_ID, shipId).apply()
    }

    private fun validadeRegistration(
        validEmail: String,
        validPassword: String,
    ): Boolean {
        if (validEmail.isEmpty() || validPassword.isEmpty()) return false
        if (validPassword.length <= 4) return false
        return true
    }

    // Max 2 vessel account creattion per user
    fun registerNewShip(newShipEmail: String) {
        loadingBar.postValue(true)
        if (sharedPref.getInt("VesselAccounts", 0) >= 2) {
            toastReceiver.postValue("Only a maximum of 2 vessel accounts per email is allowed")
            return
        }
        createVesselAccount(newShipEmail)
        registerUserInformation(userEmail, newShipEmail)
        saveShipID(newShipEmail)
        loadingBar.postValue(false)
    }

    private fun registerUserInformation(userEmail: String, newShipId: String) {
        val userSettings = hashMapOf(
            "UserPrivileges" to "List of users Admins",
            userEmail to true,
        )
        remoteDatabase.collection("VesselInfo").document(newShipId).set(userSettings)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
                toastReceiver.postValue("Failed To Register user")
            }
    }

    private fun createVesselAccount(newShipId: String) {
        remoteDatabase.collection(newShipId).get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    toastReceiver.postValue("ShipID Already Exists")
                } else {
                    val accountNumbers = sharedPref.getInt("VesselAccounts", 0)
                    if (userEmail != newShipId) {
                        toastReceiver.postValue("ShipID Created Successfully")
                    }
                    val emptydata = EMPTY_EQUIPMENT_ENTITY
                    emptydata.partNumber = "first entry"
                    remoteDatabase.collection(newShipId).document("first entry")
                        .set(emptydata)
                    remoteDatabase.collection("UserSettings").document(userEmail)
                    sharedPref.edit().putInt("VesselAccounts", accountNumbers + 1).apply()
                }
            }
            .addOnFailureListener {
                toastReceiver.postValue("Error fetching ShipID: $it")
            }
    }
}



