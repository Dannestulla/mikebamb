package com.gohan.qrmyship.login.userlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gohan.qrmyship.R
import com.gohan.qrmyship.databinding.FragmentUserLoginBinding
import com.gohan.qrmyship.login.LoginViewModel
import com.gohan.qrmyship.main_app.domain.myConstants.EMAIL
import com.gohan.qrmyship.main_app.domain.myConstants.PASSWORD
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserLogin : Fragment() {
    private var _binding: FragmentUserLoginBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseInit()
        applyBinding()
        setObservers()
    }

    private fun applyBinding() {
        binding.apply {
            editEmailAddress.setText(viewModel.loadStoredValues(EMAIL))
            editPassword.setText(viewModel.loadStoredValues(PASSWORD))
            loginButton.setOnClickListener {
                binding.progressBar.isVisible = true
                val password = editPassword.text.toString()
                val email = editEmailAddress.text.toString()
                viewModel.userEmail = email
                viewModel.signInForUserAndVessel(email, password)
                viewModel.saveEmailAndPassword(email, password)
                binding.progressBar.isVisible = false

            }
            registerNewUser.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_userRegister)
            }

            resetPassword.setOnClickListener {
                val email = editEmailAddress.text.toString()
                viewModel.passwordReset(email)
            }

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObservers() {
        viewModel.loginOK.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(R.id.action_loginFragment_to_vesselLogin)
            } else {
                binding.progressBar.isVisible = false
            }
        })
    }

    private fun firebaseInit() {
        viewModel.auth = FirebaseAuth.getInstance()
        viewModel.auth = Firebase.auth
    }
}