package com.example.mikebamb.presenter.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mikebamb.R
import com.example.mikebamb.databinding.FragmentQrBinding
import com.example.mikebamb.presenter.viewmodel.DescriptionViewModel
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult


class QRFragment : Fragment() {
    private var _binding: FragmentQrBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<DescriptionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanQr()
    }

    private fun scanQr() {
        val scanner = IntentIntegrator.forSupportFragment(this)
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        scanner.setBeepEnabled(false)
        scanner.initiateScan()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val result: IntentResult =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result.contents == null) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                val newQrCode = result.contents
                viewModel.qrCodeFromScaner = newQrCode
                findNavController().navigate(R.id.action_QRFragment_to_descriptionEquipmentFragment)
                Toast.makeText(context, "Scanned: " + result.contents, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

}
