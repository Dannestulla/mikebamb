package com.example.mikebamb.presenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mikebamb.databinding.FragmentEquipmentListBinding
import com.example.mikebamb.presenter.viewmodel.EquipmentListViewModel

class EquipmentListFragment : Fragment() {
    private var _binding: FragmentEquipmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<EquipmentListViewModel>()
    private val args: EquipmentListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val equipmentsList = args.equipmentsList
        binding.editName.text = equipmentsList
        setupRecyclerView()
        viewModel.getEquipmentsFromDatabase()
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
        val action =
            EquipmentListFragmentDirections.actionEquipmentListFragmentToDescriptionEquipmentFragment(
                partNumberClicked
            )
        findNavController().navigate(action)
    }
}