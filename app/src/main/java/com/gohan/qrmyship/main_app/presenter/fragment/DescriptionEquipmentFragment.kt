package com.gohan.qrmyship.main_app.presenter.fragment

import android.app.AlertDialog
import android.content.ContextWrapper
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.gohan.qrmyship.databinding.FragmentDescriptionEquipmentBinding
import com.gohan.qrmyship.main_app.data.local.EquipmentEntity
import com.gohan.qrmyship.main_app.domain.myConstants.USER
import com.gohan.qrmyship.main_app.presenter.viewmodel.DescriptionViewModel
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
        checkIsUser()
    }

    private fun applyBinding() {
        viewModel.equipmentDescriptionLiveData.observe(viewLifecycleOwner) {
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
                binding.shareThisQr.setOnClickListener { shareThisQrCode() }
            }
        }
        viewModel.toastReceiver.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun shareThisQrCode() {
        val equipmentQRcode = binding.editQrCode.text.toString()
        val equipmentName = binding.editEquipName.text.toString()
        if (equipmentQRcode.isNotEmpty()) {
            val qrCreated = viewModel.createQR(equipmentQRcode)
            val wrapper = ContextWrapper(context)
            val imageUri = viewModel.createQrImageFile(wrapper, qrCreated)
            sendToEmail(imageUri, equipmentQRcode, equipmentName)
        } else {
            viewModel.toastReceiver.postValue("Qr Code Field Must Have a Code!")
        }
    }

    private fun incomingFromScanerOrRecyclerView() {
        if (viewModel.qrCodeFromScaner.isEmpty()) {
            viewModel.getInDBEquipmentDescription()
        } else {
            CoroutineScope(IO).launch {
                try {
                    val result = viewModel.localGetEquipmentByQRCode(viewModel.qrCodeFromScaner)
                    viewModel.equipmentDescriptionLiveData.postValue(result)
                } catch (ex: Exception) {
                    viewModel.toastReceiver.postValue(ex.toString())

                }
            }
        }
    }

    private fun generateNewQRcode() {
        val equipmentQRnumber = binding.editPartNumber.text.toString()
        val equipmentQRname = binding.editEquipName.text.toString()
        val equipmentQRcode = getRandomString(8)
        if (equipmentQRnumber.isNotEmpty() || equipmentQRname.isNotEmpty()) {
            val qrCreated = viewModel.createQR(equipmentQRcode)
            val wrapper = ContextWrapper(context)
            val image_uri = viewModel.createQrImageFile(wrapper, qrCreated)
            binding.editQrCode.setText(equipmentQRcode)
            saveChanges()
            viewModel.toastReceiver.postValue("QR for $equipmentQRcode Created!")
            sendToEmail(image_uri, equipmentQRcode, equipmentQRname)
        } else {
            viewModel.toastReceiver.postValue("Must have valid equipment name and number!")
        }
    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun sendToEmail(image_uri: Uri?, equipmentQRcode: String, equipmentName: String) {
        val emailIntent = viewModel.shareQrCode(image_uri, equipmentQRcode, equipmentName)
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
            viewModel.toastReceiver.postValue("Saved Changes!")
        }
    }

    private fun deleteEquipment() {
        val partNumber = binding.editPartNumber.text
        val equipmentName = binding.editEquipName.text
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Are you sure you want to delete $equipmentName?")
            .setCancelable(true)
            .setPositiveButton("Yes") { dialog, id ->
                CoroutineScope(IO).launch {
                    if (viewModel.getAllEquipments().size <= 1) {
                        viewModel.toastReceiver.postValue("You should have at least one item in database.")
                        return@launch
                    }
                    viewModel.remoteDeleteEquipment(partNumber.toString().trim())
                    viewModel.localDeleteEquipment(partNumber.toString())
                    viewModel.toastReceiver.postValue("Item Deleted.")
                }
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

    fun checkIsUser() {
        if (USER) {
            binding.addQrCode.isEnabled = false
            binding.addQrCode.isVisible = false
            binding.deleteEquip.isEnabled = false
            binding.deleteEquip.isVisible = false
            binding.saveChanges.isEnabled = false
            binding.saveChanges.isVisible = false
        }
    }

    override fun onDestroyView() {
        _binding = null
        viewModel.toastReceiver = MutableLiveData<String>()
        super.onDestroyView()
    }
}


