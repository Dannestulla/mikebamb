package com.example.mikebamb.presenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.databinding.FragmentEditEquipmentBinding
import com.example.mikebamb.presenter.model.EquipmentPresenterModel.presenterComments
import com.example.mikebamb.presenter.model.EquipmentPresenterModel.presenterEquipName
import com.example.mikebamb.presenter.model.EquipmentPresenterModel.presenterFluig
import com.example.mikebamb.presenter.model.EquipmentPresenterModel.presenterInstallDate
import com.example.mikebamb.presenter.model.EquipmentPresenterModel.presenterLinkToManual
import com.example.mikebamb.presenter.model.EquipmentPresenterModel.presenterManufacturer
import com.example.mikebamb.presenter.model.EquipmentPresenterModel.presenterModel
import com.example.mikebamb.presenter.model.EquipmentPresenterModel.presenterQrCode
import com.example.mikebamb.presenter.model.EquipmentPresenterModel.presenterSerialNumber
import com.example.mikebamb.presenter.model.EquipmentPresenterModel.presenterhours
import com.example.mikebamb.presenter.viewmodel.EquipmentListViewModel


class EditEquipmentFragment : Fragment() {
    private var _binding: FragmentEditEquipmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<EquipmentListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditEquipmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addequipment.setOnClickListener { addNewItem() }
        binding.removeEquipment.setOnClickListener { }
    }

    /*private fun removeEquipment() {
        val equipmentSelected = binding.editSerialNumber.text

        val searchForEquipment = viewModel.repository.getEquipment(equipmentSelected.toString())
        if (!equipmentSelected.isNullOrBlank() || searchForEquipment.isEmpty()) {
            viewModel.repository.removeItem()
        } else {
            Toast.makeText(context,"Insert a Valid Equipment Number",Toast.LENGTH_LONG).show()
        }
    }*/

    private fun addNewItem() {
        presenterEquipName = binding.editEquipName.text.toString()
        presenterManufacturer = binding.editManufacturer.text.toString()
        presenterModel = binding.editModel.text.toString()
        presenterSerialNumber = binding.editSerialNumber.text.toString()
        presenterInstallDate = binding.editInstallDate.text.toString()
        presenterFluig = binding.editFluig.text.toString()
        presenterLinkToManual = binding.editManuallink.text.toString()
        presenterhours = binding.editHours.text.toString()
        presenterQrCode = binding.editQrCode.text.toString()
        presenterComments = binding.editComments.text.toString()
        val newItem = EquipmentEntity(
            presenterEquipName,
            presenterManufacturer,
            presenterModel,
            presenterSerialNumber,
            presenterInstallDate,
            presenterFluig,
            presenterLinkToManual,
            presenterInstallDate,
            presenterhours,
            presenterQrCode,
            presenterComments
        )
        viewModel.addNewItem(newItem)
        Toast.makeText(context,"New Item Added!",Toast.LENGTH_LONG).show()
    }
}



