package com.gohan.mikebamb.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gohan.mikebamb.main_app.domain.EquipmentConstants
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.ADMIN_LIST
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.EMAIL
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.NEW_SHIP_ACCOUNT
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.PASSWORD
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.SHARED_PREF
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.SHIP_EMAIL
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.SHIP_ID
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.SHIP_PASSWORD
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val app: Application
) : ViewModel() {
    lateinit var auth: FirebaseAuth
    var toastReceiver = MutableLiveData<String>()
    var remoteDatabase = FirebaseFirestore.getInstance()
    var shipLoginOk = MutableLiveData(false)
    var loginOK = MutableLiveData(false)
    var loadingBar = MutableLiveData(false)
    private val sharedPref: SharedPreferences =
        app.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    lateinit var userEmail: String
    lateinit var shipIdCode : String

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

    fun signIn(email: String, password: String) {
        if (validadeRegistration(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //checkIfUserOrAdmin(email)
                        userEmail = email
                        if (loginOK.value == true) {
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
            "ShipId" -> answer = sharedPref.getString(SHIP_ID, "").toString()
            "VesselPassword" -> answer =  sharedPref.getString(SHIP_PASSWORD, "").toString()
        }
        return answer
    }

    fun saveEmailAndPassword(email: String, password: String) {
        sharedPref.edit().putString(EMAIL, email).apply()
        sharedPref.edit().putString(PASSWORD, password).apply()
    }

    fun saveShipEmailAndPassword(email: String, password: String) {
        sharedPref.edit().putString(SHIP_EMAIL, email).apply()
        sharedPref.edit().putString(SHIP_PASSWORD, password).apply()
    }

    fun checkShipId(shipId: String) {
        if (shipId.isNotEmpty()) {
            remoteDatabase.collection(shipId).get()
                .addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        compareOldIdWithNewLogin(shipId)
                        //shipLoginOk.postValue(true)
                        saveShipID(shipId)
                    } else {
                        //shipLoginOk.postValue(false)
                        toastReceiver.postValue("ShipId $shipId Not Found!")
                    }
                }
                .addOnFailureListener {
                    toastReceiver.postValue("Error fetching ShipID: $it")
                }
        }
    }

    private fun compareOldIdWithNewLogin(shipId: String) {
        //Used to clean local DB in case of login in a new ship account
        val savedId = sharedPref.getString(SHIP_ID, "")
        if (savedId != shipId) {
            sharedPref.edit().putBoolean(NEW_SHIP_ACCOUNT, true).apply()
        }
    }

    private fun saveShipID(shipId: String) {
        sharedPref.edit().putString(SHIP_ID, shipId).apply()
    }

    private fun validadeRegistration(
        validEmail: String,
        validPassword: String,
    ): Boolean {
        if (validEmail.isEmpty() || validPassword.isEmpty()) return false
        if (validPassword.length <= 4) return false
        return true
    }

    // 2 vessel account per user
    fun registerNewShip(newShipEmail: String) {
        loadingBar.postValue(true)
        if (sharedPref.getInt("VesselAccounts", 0) > 2) {
            toastReceiver.postValue("Only a maximum of 2 vessel accounts per email is allowed")
            return
        }
        /*if (newShipId.trim().length < 5) {
            toastReceiver.postValue("ShipId must have at least 5 letters")
            return
        }*/
        createVesselAccount(newShipEmail)
        registerUserInformation(userEmail,newShipEmail)
        saveShipID(newShipEmail)
        loadingBar.postValue(false)
    }

    private fun registerUserInformation(userEmail: String, newShipId: String) {
        val userSettings = hashMapOf(
            userEmail to "userEmail",
            "Admin" to true,
            "ShipIdCode" to newShipId
        )
        remoteDatabase.collection("UsersInfo").document(userEmail).set(userSettings)
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
                    toastReceiver.postValue("ShipID Created Successfully")
                    val emptydata = EquipmentConstants.EMPTY_EQUIPMENT_ENTITY
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

    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}



