package com.gohan.mikebamb.presenter

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.gohan.mikebamb.databinding.ActivityLoginBinding
import com.gohan.mikebamb.presenter.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.auth = FirebaseAuth.getInstance()
        viewModel.auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.apply {
            loginButton.setOnClickListener {
                val password = editPassword.text.toString()
                val email = editEmailAddress.text.toString()
                viewModel.signIn(email, password)
            }
            binding.editEmailAddress.setText(viewModel.loadSavedPref("Email"))
            binding.editPassword.setText(viewModel.loadSavedPref("Password"))
        }
        viewModel.loginOK.observe(this, {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }
}
