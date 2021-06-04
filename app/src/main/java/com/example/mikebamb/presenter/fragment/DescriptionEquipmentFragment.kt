package com.example.mikebamb.presenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.databinding.FragmentDescriptionEquipmentBinding
import com.example.mikebamb.presenter.viewmodel.EquipmentListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DescriptionEquipmentFragment : Fragment() {
    private var _binding: FragmentDescriptionEquipmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<EquipmentListViewModel>()
    private var equipmentDescription = MutableLiveData<EquipmentEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionEquipmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInDBEquipmentDescription()
        applyBinding()
    }

    private fun getInDBEquipmentDescription() {
        CoroutineScope(Dispatchers.IO).launch {
            val equipmentDescriptionData = viewModel.getEquipment(
                viewModel.partNumberClicked
            )
            equipmentDescription.postValue(equipmentDescriptionData)
        }
    }

    private fun applyBinding() {
        equipmentDescription.observe(viewLifecycleOwner, {
            binding.apply {
                editEquipName.setText(it.equip_name)
                editManufacturer.setText(it.manufacturer)
                editModel.setText(it.model)
                editSerialNumber.setText(it.partNumber)
                editInstallDate.setText(it.installdate)
                editFluig.setText(it.fluig)
                editManuallink.setText(it.manual_links)
                editHours.setText(it.hoursEntity)
                editQrCode.setText(it.qrCodeEntity)
                editComments.setText(it.comments)
                saveChanges.setOnClickListener { saveChanges() }
            }
        })
    }

    private fun saveChanges() {
        /*binding.apply {
            editEquipName.text = viewModel.
            editManufacturer.
            editModel.
            editSerialNumber.
            editInstallDate.
            editFluig.
            editManuallink.
            editHours.
            editQrCode.
            editComments.
            }*/
        }
    }
