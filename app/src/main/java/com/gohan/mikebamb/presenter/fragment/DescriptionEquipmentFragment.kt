package com.gohan.mikebamb.presenter.fragment

import android.app.AlertDialog
import android.content.ContextWrapper
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.gohan.mikebamb.data.local.EquipmentEntity
import com.gohan.mikebamb.databinding.FragmentDescriptionEquipmentBinding
import com.gohan.mikebamb.presenter.viewmodel.DescriptionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.*


class DescriptionEquipmentFragment : Fragment() {
    private var _binding: FragmentDescriptionEquipmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<DescriptionViewModel>()
    private val args: DescriptionEquipmentFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionEquipmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.partNumberClicked = args.partNumber
        incomingFromScanerOrRecyclerView()
        applyBinding()
        hideActionBar()
    }

    private fun applyBinding() {
        viewModel.equipmentDescriptionLiveData.observe(viewLifecycleOwner, {
            binding.apply {
                editEquipName.setText(it.equipNameEntity)
                editManufacturer.setText(it.manufacturerEntity)
                editModel.setText(it.modelEntity)
                editPartNumber.setText(it.partNumber)
                editInstallDate.setText(it.installdateEntity)
                editOrderNumber.setText(it.fluigEntity)
                editManuallink.setText(it.manualLinksEntity)
                editHours.setText(it.hoursEntity)
                editQrCode.setText(it.qrCodeEntity)
                editComments.setText(it.commentsEntity)
                editCategory1.setText(it.category1Entity)
                editCategory2.setText(it.category2Entity)
                editCategory3.setText(it.category3Entity)
                editObservations1.setText(it.observations1Entity)
                editObservations2.setText(it.observations2Entity)
                editObservations3.setText(it.observations3Entity)
                editObservations4.setText(it.observations4Entity)
                editObservations5.setText(it.observations5Entity)
                saveChanges.setOnClickListener { saveChanges() }
                deleteEquip.setOnClickListener { deleteEquipment() }
                binding.addQrCode.setOnClickListener { generateNewQRcode() }
                // scanQrDescription.setOnClickListener { }
                // printAllQrDatabase.setOnClickListener { printAllQrDatabase() }
            }
        })
    }

    private fun incomingFromScanerOrRecyclerView() {
        if (viewModel.qrCodeFromScaner.isNullOrEmpty()) {
            viewModel.getInDBEquipmentDescription()
        } else {
            CoroutineScope(IO).launch {
                try {
                    val result = viewModel.localGetEquipmentByQRCode(viewModel.qrCodeFromScaner)
                    viewModel.equipmentDescriptionLiveData.postValue(result)
                } catch (ex: Exception) {
                    Toast.makeText(context, "ERROR: $ex", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun generateNewQRcode() {
        val equipmentQRnumber = binding.editPartNumber.text.toString()
        val equipmentQRname = binding.editEquipName.text.toString()
        val equipmentQRcode = (equipmentQRnumber + equipmentQRname).replace(" ", "")
        if (equipmentQRnumber.isNotEmpty() || equipmentQRname.isNotEmpty()) {
            val qrCreated = viewModel.createQR(equipmentQRcode)
            binding.generatedQr.setImageBitmap(qrCreated)
            val wrapper = ContextWrapper(context)
            val image_uri = viewModel.createQrImageFile(wrapper, qrCreated)
            binding.editQrCode.setText(equipmentQRcode)
            saveChanges()
            Toast.makeText(context, "QR for $equipmentQRcode Created!", Toast.LENGTH_LONG).show()
            sendToEmail(image_uri, equipmentQRcode)
        } else {
            Toast.makeText(context, "Must have valid equipment name and number!", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun sendToEmail(image_uri: Uri?, equipmentQRcode: String) {
        val emailIntent = viewModel.shareQrCode(image_uri, equipmentQRcode)
        startActivity(emailIntent)
    }

    private fun saveChanges() {
        binding.apply {
            val newItem = EquipmentEntity(
                editPartNumber.text.toString(),
                editEquipName.text.toString(),
                editModel.text.toString(),
                editManufacturer.text.toString(),
                editManuallink.text.toString(),
                editOrderNumber.text.toString(),
                editInstallDate.text.toString(),
                editHours.text.toString(),
                editQrCode.text.toString(),
                editComments.text.toString(),
                editCategory1.text.toString(),
                editCategory2.text.toString(),
                editCategory3.text.toString(),
                editObservations1.text.toString(),
                editObservations2.text.toString(),
                editObservations3.text.toString(),
                editObservations4.text.toString(),
                editObservations5.text.toString(),
                Timestamp(Date().time).toString(),
            )
            viewModel.localAddNewItem(newItem)
            viewModel.remoteAddNewItem(newItem)
            Toast.makeText(context, "Saved Changes!", Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEquipment() {
        val partNumber = binding.editPartNumber.text
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Are you sure you want to delete $partNumber?")
            .setCancelable(true)
            .setPositiveButton("Yes") { dialog, id ->
                CoroutineScope(IO).launch {
                    viewModel.localDeleteEquipment(partNumber.toString().trim())
                    viewModel.remoteDeleteEquipment(partNumber.toString().trim())
                    }
                Toast.makeText(context, "Item Deleted.", Toast.LENGTH_LONG).show()
                viewModel.equipmentDescriptionLiveData.postValue(
                    viewModel.emptyEquipmentEntity
                )
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun printAllQrDatabase() {
        viewModel.localPrintAllQrCodes()
    }

    fun hideActionBar() {
        return (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


