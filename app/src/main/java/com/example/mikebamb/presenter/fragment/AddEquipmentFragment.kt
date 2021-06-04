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
import com.example.mikebamb.presenter.model.EquipmentPresenterModel.presenterPartNumber
import com.example.mikebamb.presenter.model.EquipmentPresenterModel.presenterhours
import com.example.mikebamb.presenter.viewmodel.EquipmenViewModel


class AddEquipmentFragment : Fragment() {
    private var _binding: FragmentEditEquipmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<EquipmenViewModel>()

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
    }

    private fun addNewItem() {
        presenterEquipName = binding.editEquipName.text.toString()
        presenterManufacturer = binding.editManufacturer.text.toString()
        presenterModel = binding.editModel.text.toString()
        presenterPartNumber = binding.editPartNumber.text.toString()
        presenterInstallDate = binding.editInstallDate.text.toString()
        presenterFluig = binding.editFluig.text.toString()
        presenterLinkToManual = binding.editManuallink.text.toString()
        presenterhours = binding.editHours.text.toString()
        presenterQrCode = binding.editQrCode.text.toString()
        presenterComments = binding.editComments.text.toString()
        val newItem = EquipmentEntity(
            presenterPartNumber,
            presenterEquipName,
            presenterModel,
            presenterManufacturer,
            presenterLinkToManual,
            presenterFluig,
            presenterInstallDate,
            presenterhours,
            presenterQrCode,
            presenterComments
        )
        viewModel.addNewItem(newItem)
        Toast.makeText(context,"New Item Added!",Toast.LENGTH_LONG).show()
    }
}



