package com.example.mikebamb.presenter.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mikebamb.R
import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.databinding.FragmentEquipmentListBinding
import com.example.mikebamb.presenter.viewmodel.EquipmentAdapter
import com.example.mikebamb.presenter.viewmodel.EquipmentListViewModel

class EquipmentListFragment : Fragment() {
    private var _binding: FragmentEquipmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<EquipmentListViewModel>()
    private val mAdapter = EquipmentAdapter()
    private lateinit var itemFromList : ArrayList<EquipmentEntity>
    private lateinit var selectedItem : EquipmentEntity
    private val equipmentAdapter = EquipmentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBindings()
        viewModel.getListFromDatabase()
        refreshRecyclerView()
        equipmentAdapter.myClick.observe(viewLifecycleOwner, {
            findNavController().navigate(R.id.action_equipmentListFragment_to_descriptionEquipmentFragment) })
    }

    private fun applyBindings() {
        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun refreshRecyclerView() {
        viewModel.recyclerViewItems.observe(viewLifecycleOwner, {mAdapter.submitList(it)})
    }



    /*override fun onClickListener(position: Int) {
        selectedItem = itemFromList[position]
        findNavController().navigate(R.id.action_equipmentListFragment_to_descriptionEquipmentFragment)
        }*/
}