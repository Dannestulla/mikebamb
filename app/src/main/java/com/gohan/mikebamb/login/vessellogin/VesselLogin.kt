package com.gohan.mikebamb.login.vessellogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gohan.mikebamb.R
import com.gohan.mikebamb.databinding.FragmentVesselLoginBinding
import com.gohan.mikebamb.login.LoginViewModel
import com.gohan.mikebamb.main_app.MainActivity
import com.gohan.mikebamb.main_app.domain.myConstants

class VesselLogin : Fragment() {

    private var _binding: FragmentVesselLoginBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVesselLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBinding()
        setObservers()
        loadSavedShipId()
    }

    private fun applyBinding() {
        binding.apply {
            resetVesselPassword.setOnClickListener {
                binding.progressBar.isVisible = true
                viewModel.passwordReset(binding.editVesselName.text.toString())
                viewModel.loadingBar.postValue(false)
            }
            buttonLogShip.setOnClickListener {
                binding.progressBar.isVisible = true
                val vesselEmail = binding.editVesselName.text.toString()
                val vesselPassword = binding.editVesselPassword.text.toString()
                viewModel.checkShipId(vesselEmail)
                viewModel.signInForUserAndVessel(vesselEmail, vesselPassword)
                viewModel.loadingBar.postValue(false)
            }
            registerNewVessel.setOnClickListener {
                findNavController().navigate(R.id.action_vesselLogin_to_vesselRegister)
            }
        }
    }

    private fun setObservers() {
        viewModel.shipLoginOk.observe(viewLifecycleOwner, {
            if (it) {
                binding.progressBar.isVisible = true
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            } else {
                binding.progressBar.isVisible = false
            }
        })
        viewModel.loadingBar.observe(viewLifecycleOwner, {
            binding.progressBar.isVisible = it
        })
    }

    private fun loadSavedShipId() {
        binding.editVesselName.setText(viewModel.loadStoredValues(myConstants.VESSEL_ID))
        binding.editVesselPassword.setText(viewModel.loadStoredValues(myConstants.VESSEL_PASSWORD))
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

