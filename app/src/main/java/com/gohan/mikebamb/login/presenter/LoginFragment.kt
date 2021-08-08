package com.gohan.mikebamb.login.presenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gohan.mikebamb.R
import com.gohan.mikebamb.databinding.FragmentLoginBinding
import com.gohan.mikebamb.main_app.domain.myConstants.EMAIL
import com.gohan.mikebamb.main_app.domain.myConstants.PASSWORD
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseInit()
        applyBinding()
        setObservers()
    }

    private fun applyBinding() {
        binding.apply {
            editEmailAddress.setText(viewModel.loadStoredValues(EMAIL))
            editPassword.setText(viewModel.loadStoredValues(PASSWORD))
            loginButton.setOnClickListener {
                binding.progressBar.isVisible = true
                val password = editPassword.text.toString()
                val email = editEmailAddress.text.toString()
                viewModel.userEmail = email
                viewModel.signInForUserAndVessel(email, password)
                viewModel.saveEmailAndPassword(email, password)
            }

            registerButton.setOnClickListener {
                val password = editPassword.text.toString()
                val email = editEmailAddress.text.toString()
                viewModel.createAccount(email, password)
            }
            resetPassword.setOnClickListener {
                val email = editEmailAddress.text.toString()
                viewModel.passwordReset(email)
            }
        }
    }

    private fun setObservers() {
        viewModel.loginOK.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(R.id.action_loginFragment_to_registerShipFragment)
            } else {
                binding.progressBar.isVisible = false
            }
        })
    }

    private fun firebaseInit() {
        viewModel.auth = FirebaseAuth.getInstance()
        viewModel.auth = Firebase.auth
    }
}