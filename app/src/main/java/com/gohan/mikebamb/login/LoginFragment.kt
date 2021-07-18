package com.gohan.mikebamb.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gohan.mikebamb.R
import com.gohan.mikebamb.databinding.FragmentCategoryBinding
import com.gohan.mikebamb.databinding.FragmentLoginBinding
import com.gohan.mikebamb.main_app.MainActivity
import com.gohan.mikebamb.main_app.domain.EquipmentConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseInit()
        applyBinding()
        setObservers()
    }

    private fun applyBinding() {
        binding.apply{
            editEmailAddress.setText(viewModel.loadSavedPref(EquipmentConstants.EMAIL))
            editPassword.setText(viewModel.loadSavedPref(EquipmentConstants.PASSWORD))
            shipId.setText(viewModel.loadSavedPref(EquipmentConstants.SHIP_ID))
            loginButton.setOnClickListener {
                val password = editPassword.text.toString()
                val email = editEmailAddress.text.toString()
                val shipId = shipId.text.toString()
                viewModel.signIn(email, password, shipId)
            }
            registerNewShip.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerShipFragment)
        }
    }}


    private fun setObservers() {
        viewModel.loginOK.observe(viewLifecycleOwner, {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        })
        viewModel.toastReceiver.observe(viewLifecycleOwner, {
            toasterMaker(it)
        })
    }

    private fun firebaseInit() {
        viewModel.auth = FirebaseAuth.getInstance()
        viewModel.auth = Firebase.auth
    }

    private fun toasterMaker(toastMessage : String) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
    }
}