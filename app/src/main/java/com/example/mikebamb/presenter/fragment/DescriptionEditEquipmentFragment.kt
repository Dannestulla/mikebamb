package com.example.mikebamb.presenter.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.databinding.FragmentDescriptionEquipmentBinding
import com.example.mikebamb.presenter.viewmodel.EquipmenViewModel
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class DescriptionEditEquipmentFragment : Fragment() {
    private var _binding: FragmentDescriptionEquipmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<EquipmenViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionEquipmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBinding()
        incomingFromScanerOrRecyclerView()
        applyBinding()
        binding.addQrCode.setOnClickListener { clickOnCreateQR() }
    }

    private fun incomingFromScanerOrRecyclerView() {
        if (viewModel.qrCodeFromScaner.isEmpty()) {
            viewModel.getInDBEquipmentDescription()
        } else {
            viewModel.getEquipmentByQrCode()
        }
    }


    private fun clickOnCreateQR() {
        val equipmentNumber = binding.editEquipName.text.toString()
        if (equipmentNumber.isNotEmpty()) {
            val qrCreated = viewModel.createQR(equipmentNumber)
            binding.generatedQr.setImageBitmap(qrCreated)
            Toast.makeText(context, "QR for $qrCreated Created!", Toast.LENGTH_LONG).show()
            binding.editQrCode.setText(qrCreated.toString())
            saveChanges()
        }
    }



    private fun applyBinding() {
        viewModel.equipmentDescriptionLiveData.observe(viewLifecycleOwner, {
            binding.apply {
                editEquipName.setText(it.equipNameEntity)
                editManufacturer.setText(it.manufacturerEntity)
                editModel.setText(it.modelEntity)
                editPartNumber.setText(it.partNumber)
                editInstallDate.setText(it.installdateEntity)
                editFluig.setText(it.fluigEntity)
                editManuallink.setText(it.manualLinksEntity)
                editHours.setText(it.hoursEntity)
                editQrCode.setText(it.qrCodeEntity)
                editComments.setText(it.commentsEntity)
                saveChanges.setOnClickListener { saveChanges() }
                scanQrDescription.setOnClickListener { }
                deleteEquip.setOnClickListener { deleteEquipment() }
            }
        })

    }

    private fun deleteEquipment() {
        val partNumber = binding.editPartNumber.text
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Are you sure you want to Delete?")
            .setCancelable(true)
            .setPositiveButton("Yes") { dialog, id ->
                CoroutineScope(IO).launch { viewModel.deleteEquipment(partNumber.toString()) }
                Toast.makeText(context, "Item Deleted", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        viewModel.equipmentDescriptionLiveData.postValue(
            EquipmentEntity("", "", "", "", "", "", "", "", "", "")
        )
        val alert = builder.create()
        alert.show()

    }

    private fun saveChanges() {
        binding.apply {
            val newItem = EquipmentEntity(
                editPartNumber.text.toString(),
                editEquipName.text.toString(),
                editModel.text.toString(),
                editManufacturer.text.toString(),
                editManuallink.text.toString(),
                editFluig.text.toString(),
                editInstallDate.text.toString(),
                editHours.text.toString(),
                editQrCode.text.toString(),
                editComments.text.toString()
            )
            viewModel.addNewItem(newItem)
            Toast.makeText(context, "Saved Changes!", Toast.LENGTH_LONG).show()
        }
    }

}


