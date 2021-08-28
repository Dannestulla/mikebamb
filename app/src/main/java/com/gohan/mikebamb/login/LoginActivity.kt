package com.gohan.mikebamb.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.gohan.mikebamb.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        viewModel.toastReceiver.observe(this, {
            toasterMaker(it)
        })
    }

    private fun toasterMaker(toastMessage : String) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
    }
}
