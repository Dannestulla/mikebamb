package com.example.mikebamb.presenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mikebamb.R
import com.example.mikebamb.databinding.FragmentEquipmentListBinding
import com.example.mikebamb.presenter.viewmodel.EquipmenViewModel

class EquipmentListFragment : Fragment() {
    private var _binding: FragmentEquipmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<EquipmenViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipmentListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getListFromDatabase()
        setupRecyclerView()
        defaultDBStartup()
    }

    private fun defaultDBStartup() {
        viewModel.addNewItem(DBData().itemOne)
        viewModel.addNewItem(DBData().itemTwo)
        viewModel.addNewItem(DBData().itemThree)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = viewModel.mAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        viewModel.mAdapter.onItemClick = { position ->
            navigateToDescription(position)
        }
        viewModel.recyclerViewItems.observe(
            viewLifecycleOwner,
            { viewModel.mAdapter.submitList(it) })

    }

    private fun navigateToDescription(position: Int) {
        val partNumberClicked = viewModel.recyclerViewItems.value?.get(position)?.partNumber!!
        viewModel.partNumberClicked = partNumberClicked
        findNavController().navigate(R.id.action_equipmentListFragment_to_descriptionEquipmentFragment)
    }
}