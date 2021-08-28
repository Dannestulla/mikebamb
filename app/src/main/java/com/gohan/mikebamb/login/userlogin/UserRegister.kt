package com.gohan.mikebamb.login.userlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gohan.mikebamb.R
import com.gohan.mikebamb.databinding.FragmentUserRegisterBinding
import com.gohan.mikebamb.login.LoginViewModel

class UserRegister : Fragment() {
    private var _binding: FragmentUserRegisterBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBinding()

    }

    private fun applyBinding() {
        binding.apply {
            registerButton2.setOnClickListener {
                val password = editPassword.text.toString()
                val email = editEmailAddress.text.toString()
                val confirmPassword = confirmPassword.text.toString()
                if (confirmPassword == password) {
                    viewModel.createAccount(email, password)
                    findNavController().navigate(R.id.action_userRegister_to_loginFragment)
                } else {
                    viewModel.toastReceiver.postValue("Passwords Must Match!")
                    viewModel.loadingBar.postValue(false)
                    binding.progressBar.isVisible = false
                }
            }
            backToLogin.setOnClickListener {
                findNavController().navigate(R.id.action_userRegister_to_loginFragment)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}