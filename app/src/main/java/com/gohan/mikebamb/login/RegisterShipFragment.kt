package com.gohan.mikebamb.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.gohan.mikebamb.R
import com.gohan.mikebamb.databinding.FragmentAddEquipmentBinding
import com.gohan.mikebamb.databinding.FragmentRegisterShipBinding

class RegisterShipFragment : Fragment() {
    private var _binding: FragmentRegisterShipBinding? = null
    private val binding get() = _binding!!
    val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterShipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonRegister.setOnClickListener {
            registerNewShip(binding.editVesselName.text.toString())
        }
    }

    private fun registerNewShip(newShipId : String) {
        viewModel.registerNewShip(newShipId)
    }
}