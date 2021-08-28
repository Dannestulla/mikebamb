package com.gohan.mikebamb.login.vessellogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gohan.mikebamb.R
import com.gohan.mikebamb.databinding.FragmentVesselLoginBinding
import com.gohan.mikebamb.databinding.FragmentVesselRegisterBinding
import com.gohan.mikebamb.login.LoginViewModel

class VesselRegister : Fragment() {
    private var _binding: FragmentVesselRegisterBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVesselRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBinding()
    }

    private fun applyBinding() {
        binding.registerVesselButton.setOnClickListener {
            binding.progressBar.isVisible = true
            val vesselEmail = binding.editEmailAddress.text.toString()
            val vesselPassword =  binding.editPassword.text.toString()
            val vesselConfirmPassword = binding.confirmPassword.text.toString()
            if (vesselPassword == vesselConfirmPassword) {
                viewModel.createAccount(vesselEmail, vesselPassword)
                viewModel.registerNewShip(vesselEmail)
                viewModel.loadingBar.postValue(false)
                findNavController().navigate(R.id.action_vesselRegister_to_vesselLogin)
            } else {
                viewModel.toastReceiver.postValue("Passwords Must Match!")
                viewModel.loadingBar.postValue(false)
                binding.progressBar.isVisible = false
            }
        }
        binding.backToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_vesselRegister_to_vesselLogin)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
