package com.example.mikebamb.presenter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mikebamb.R
import com.example.mikebamb.databinding.FragmentMenuBinding
import dagger.hilt.android.AndroidEntryPoint


class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addequipment.setOnClickListener { findNavController().navigate(R.id.action_menuFragment_to_addEquipmentFragment) }
        binding.equipmentlist.setOnClickListener { findNavController().navigate(R.id.action_menuFragment_to_equipmentListFragment) }
    }
}
