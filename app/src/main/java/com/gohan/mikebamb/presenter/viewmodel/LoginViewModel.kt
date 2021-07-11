package com.gohan.mikebamb.presenter.viewmodel

import android.app.Application
import android.content.Context
import android.provider.Telephony.Carriers.PASSWORD
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gohan.mikebamb.domain.EquipmentConstants
import com.gohan.mikebamb.domain.EquipmentConstants.myConstants.SHARED_PREF
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val app: Application
) : ViewModel() {
    var loginOK = MutableLiveData(false)
    lateinit var auth: FirebaseAuth

    fun createAccount(email: String, password: String, confpassword: String) {
        if (validadeRegistration(email, password, confpassword)) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(app, "Registration Complete", Toast.LENGTH_LONG).show()
                    sendEmailVerification()
                    //loginOK.postValue("registerToCuisine")
                } else {
                    val ex = task.exception.toString()
                    Toast.makeText(app, "Create Account Failed, $ex", Toast.LENGTH_LONG).show()
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
        if (validadeRegistration(email, password, password))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(app, "Login Successfull", Toast.LENGTH_LONG).show()
                        savePref(email, password)
                        loginOK.postValue(true)
                    } else {
                        val ex = task.exception.toString()
                        Toast.makeText(app, "SignIn Failed : $ex", Toast.LENGTH_LONG).show()
                    }
                }
        checkIsUser(email)
    }

    private fun checkIsUser(email: String) {
        EquipmentConstants.USER = !EquipmentConstants.ADMIN_LIST.contains(email)
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
            "Email" -> answer = sharedPref.getString(EquipmentConstants.EMAIL, "").toString()
            "Password" -> answer = sharedPref.getString(EquipmentConstants.PASSWORD, "").toString()
        }
        return answer
    }

    private fun savePref(email: String, password: String) {
        val sharedPref = app.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        sharedPref.edit().putString(EquipmentConstants.EMAIL, email).apply()
        sharedPref.edit().putString(PASSWORD, password).apply()
    }

    companion object {
        fun validadeRegistration(
            validEmail: String,
            validPassword: String,
            validConfirmedPassword: String
        ): Boolean {
            if (validEmail.isEmpty() || validPassword.isEmpty() || validConfirmedPassword.isEmpty()) return false
            if (validPassword.length <= 4) return false
            if (validPassword != validConfirmedPassword) return false
            return true
        }
    }
}


