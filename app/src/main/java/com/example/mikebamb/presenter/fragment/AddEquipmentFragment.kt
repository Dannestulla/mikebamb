package com.example.mikebamb.presenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mikebamb.data.toEquipmentEntity
import com.example.mikebamb.databinding.FragmentEditEquipmentBinding
import com.example.mikebamb.domain.EquipmentModel
import com.example.mikebamb.presenter.viewmodel.DescriptionViewModel
import java.sql.Timestamp
import java.util.*


class AddEquipmentFragment : Fragment() {
    private var _binding: FragmentEditEquipmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<DescriptionViewModel>()

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
        val newItem = EquipmentModel(
            nameModel = binding.editEquipName.text.toString(),
            manufacturerModel = binding.editManufacturer.text.toString(),
            modelModel = binding.editModel.text.toString(),
            partNumberModel = binding.editPartNumber.text.toString(),
            installDateModel = binding.editInstallDate.text.toString(),
            fluigModel = binding.editFluig.text.toString(),
            linkToManualModel = binding.editManuallink.text.toString(),
            hoursModel = binding.editHours.text.toString(),
            qrCodeModel = binding.editQrCode.text.toString(),
            commentsModel = binding.editComments.text.toString(),
            category1Model = binding.editComments.text.toString(),
            category2Model = binding.editCategory2.text.toString(),
            category3Model = binding.editCategory3.text.toString(),
            observation1Model = binding.observation1.text.toString(),
            observation2Model = binding.observation2.text.toString(),
            observation3Model = binding.observation3.text.toString(),
            observation4Model = binding.observation4.text.toString(),
            observation5Model = binding.observation5.text.toString(),
            timestamp = Timestamp(Date().time),
        )

        viewModel.addNewItem(newItem.toEquipmentEntity())
        Toast.makeText(context, "New Item Added!", Toast.LENGTH_LONG).show()
    }
}



