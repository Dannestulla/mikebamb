package com.example.mikebamb.presenter.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mikebamb.data.remote.CloudFirestore
import com.example.mikebamb.databinding.FragmentCategoryBinding
import com.example.mikebamb.presenter.adapter.CategoryAdapter
import com.example.mikebamb.presenter.viewmodel.CategoryViewModel

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<CategoryViewModel>()
    private var cloudFirestore = CloudFirestore
    private var mAdapter = CategoryAdapter()

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
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
        //defaultValues()
        //viewModel.localGetAllEquipments()
        initializeAndGetRemoteData()
        getLocalCategoryList()
        observeRemoteChangesAndCompareRemoteToLocal()
        mAdapter.onItemClick = { position ->
            handleItemClick(position)
        }
        hideActionBar()
    }

    private fun handleItemClick(position: Int) {
        if (!viewModel.exitMainCategory) {
            val subCategory = viewModel.categorySelected[position]
            viewModel.localGetSubCategory(subCategory)
            binding.editCategory.text = subCategory
            binding.textCategory.text = "Sub Category: "
        } else if (!viewModel.exitSubCategory) {
            val subSubCategory = viewModel.categorySelected[position]
            viewModel.localGetSubSubCategory(subSubCategory)
            binding.editCategory.text = subSubCategory
            binding.textCategory.text = "SubSub Category: "
        } else {
            navigateToEquipmentList(position)
        }
    }

    private fun observeRemoteChangesAndCompareRemoteToLocal() {
        cloudFirestore.documentsLiveData.observe(viewLifecycleOwner, {
            viewModel.remoteDBdata = it
            viewModel.compareRemoteAndLocalData(it)
        })
    }

    private fun initializeAndGetRemoteData() {
        if (!viewModel.checkedRemote) {
            viewModel.remoteInitializeDatabase()
            viewModel.remoteGetAllData()
        }
    }

    private fun getLocalCategoryList() {
        viewModel.localGetMainCategory()
        viewModel.currentCategory.observe(viewLifecycleOwner, {
            mAdapter.submitList(it)
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
        viewModel.exitMainCategory = false
        viewModel.exitSubCategory = false
    }

    fun hideActionBar() {
        return (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}