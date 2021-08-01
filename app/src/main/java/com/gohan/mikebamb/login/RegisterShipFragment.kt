package com.gohan.mikebamb.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.gohan.mikebamb.databinding.FragmentRegisterShipBinding
import com.gohan.mikebamb.main_app.MainActivity
import com.gohan.mikebamb.main_app.domain.EquipmentConstants
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.NEW_SHIP_ACCOUNT
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.SHIP_ID
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.SHIP_PASSWORD

class RegisterShipFragment : Fragment() {
    private var _binding: FragmentRegisterShipBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterShipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBinding()
        setObservers()
        loadSavedShipId()
    }

    private fun loadSavedShipId() {
        binding.editVesselName.setText(viewModel.loadStoredValues(SHIP_ID))
        binding.editVesselPassword.setText(viewModel.loadStoredValues(SHIP_PASSWORD))
    }

    private fun applyBinding() {
        binding.buttonRegister.setOnClickListener {
            binding.progressBar.isVisible = true
            val vesselEmail = binding.editVesselName.text.toString()
            val vesselPassword =  binding.editVesselPassword.text.toString()
            viewModel.createAccount(vesselEmail, vesselPassword)
            viewModel.registerNewShip(vesselEmail)
            viewModel.loadingBar.postValue(false)
        }
        binding.buttonLogShip.setOnClickListener {
            binding.progressBar.isVisible = true
            val vesselEmail = binding.editVesselName.text.toString()
            val vesselPassword =  binding.editVesselPassword.text.toString()
            viewModel.checkShipId(vesselEmail)
            viewModel.saveShipEmailAndPassword(vesselEmail, vesselPassword)
            viewModel.signIn(vesselEmail, vesselPassword)
            viewModel.loadingBar.postValue(false)
        }
        binding.resetVesselPassword.setOnClickListener {
            binding.progressBar.isVisible = true
            viewModel.passwordReset(binding.editVesselName.text.toString())
            viewModel.loadingBar.postValue(false)
        }
    }

    private fun setObservers() {
        viewModel.shipLoginOk.observe(viewLifecycleOwner, {
            if (it) {
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
}
