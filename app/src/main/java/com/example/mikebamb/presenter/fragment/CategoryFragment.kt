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
import com.example.mikebamb.data.remote.CloudFirestore
import com.example.mikebamb.data.remote.CloudFirestore.Companion.documentsLiveData
import com.example.mikebamb.databinding.FragmentCategoryBinding
import com.example.mikebamb.presenter.viewmodel.CategoryViewModel

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<CategoryViewModel>()
    var cloudFirestore = CloudFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.categoryRecyclerview.apply {
            adapter = viewModel.mAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        defaultValues()
        viewModel.getListFromDatabase()
        initAndCheckRemoteDB()
        getMainCategory()
        observableRemoteData()
        viewModel.mAdapter.onItemClick = { position ->
            if (!viewModel.exitCategoryMenu) {
                val subCategory = viewModel.categorySelected[position]
                viewModel.getSubCategory(subCategory)
                binding.editCategory.text = subCategory
                binding.textCategory.text = "Sub Category: "
            } else {
                navigateToEquipmentList(position)
            }
        }

    }

    private fun observableRemoteData() {
        cloudFirestore.documentsLiveData.observe(viewLifecycleOwner, {
            viewModel.remoteDBdata = it
            Log.e("remote", "$it \n")
            viewModel.compareRemoteAndLocalData(it)
        })
    }

    private fun defaultValues() {
        binding.editCategory.text = ""
    }

    private fun initAndCheckRemoteDB() {
        if (!viewModel.checkedRemote) {
            viewModel.initializeRemoteDatabase()
            viewModel.getAllRemoteData()
        }
    }

    private fun getMainCategory() {
        viewModel.getMainCategory()
        viewModel.currentCategory.observe(viewLifecycleOwner, {
            viewModel.mAdapter.submitList(it)
        }
        )
    }

    private fun navigateToEquipmentList(position: Int) {
        val equipmentCategory = viewModel.categorySelected[position]
        Log.d("equi", equipmentCategory)
        val action = CategoryFragmentDirections.actionCategoryFragmentToEquipmentListFragment(
            equipmentCategory
        )
        findNavController().navigate(action)
    }

    override fun onPause() {
        super.onPause()
        viewModel.exitCategoryMenu = false
    }
}