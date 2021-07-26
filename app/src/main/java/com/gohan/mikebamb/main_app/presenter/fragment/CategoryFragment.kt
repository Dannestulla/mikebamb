package com.gohan.mikebamb.main_app.presenter.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.gohan.mikebamb.databinding.FragmentCategoryBinding
import com.gohan.mikebamb.main_app.data.remote.CloudFirestore.Companion.documentsLiveData
import com.gohan.mikebamb.main_app.domain.EquipmentConstants
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.NEW_SHIP_ACCOUNT
import com.gohan.mikebamb.main_app.domain.EquipmentConstants.myConstants.SHIP_ID
import com.gohan.mikebamb.main_app.presenter.adapter.CategoryAdapter
import com.gohan.mikebamb.main_app.presenter.viewmodel.CategoryViewModel
import kotlin.properties.Delegates

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<CategoryViewModel>()
    private var mAdapter = CategoryAdapter()
    private var positionMain by Delegates.notNull<Int>()
    private var positionSubSub by Delegates.notNull<Int>()
    private var positionSub by Delegates.notNull<Int>()
    private var subCategory = " "
    private var subSubCategory = " "

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        applyBinding()
        viewModel.remoteInitializeDatabase()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSharedPref()
        getOnlineDataOnce()
        mAdapter.onItemClick = { position ->
            handleItemClick(position)
        }
        localGetMainCategory()
        onBackPressed()
    }

    private fun getOnlineDataOnce() {
        if (viewModel.firstStart) {
            viewModel.remoteGetAllData()
            observeRemoteChangesAndCompareRemoteToLocal()
            viewModel.firstStart = false
        }
    }

    private fun applyBinding() {
        binding.apply {
            categoryRecyclerview.apply {
                adapter = mAdapter
                layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            }
            account.setOnClickListener {
                copyToClipBoard(binding.account.text)
                Toast.makeText(context, "Ship Code Copied to Clipboard!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun copyToClipBoard(accountId: CharSequence?) {
        val clipboard = getSystemService(
            requireContext(),
            ClipboardManager::class.java
        )
        val clip = ClipData.newPlainText("ShipAccount:", accountId)
        clipboard?.setPrimaryClip(clip)
    }

    private fun loadSharedPref() {
        val sharedPref =
            context?.getSharedPreferences(EquipmentConstants.SHARED_PREF, Context.MODE_PRIVATE)
        binding.account.text = sharedPref?.getString(SHIP_ID, "")
        if (sharedPref!!.getBoolean(NEW_SHIP_ACCOUNT, false)) {
            viewModel.localDeleteAllData()
            sharedPref.edit().remove(NEW_SHIP_ACCOUNT).apply()
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.exitToSubSubCategory) {
                        getSecondCategory(positionSubSub)
                        viewModel.exitToSubSubCategory = false
                        viewModel.exitToSubCategory = false
                        viewModel.exitToMainCategory = false
                    }
                    if (viewModel.exitToSubCategory) {
                        getSecondCategory(positionSub)
                        viewModel.exitToSubCategory = false
                        viewModel.exitToMainCategory = false
                    }
                    if (viewModel.exitToMainCategory) {
                        localGetMainCategory()
                        viewModel.exitToMainCategory = false
                        binding.textCategory.text = "Categories:"
                        binding.editCategory.text = ""
                    }
                }
            })
    }

    private fun handleItemClick(position: Int) {
        this.positionMain = position
        if (!viewModel.exitToMainCategory) {
            getSecondCategory(position)
        } else if (!viewModel.exitToSubCategory) {
            getThirdCategory(position)
        } else {
            navigateToEquipmentList(position)
        }
    }

    private fun localGetMainCategory() {
        viewModel.localGetMainCategory()
        viewModel.currentCategory.observe(viewLifecycleOwner, {
            mAdapter.submitList(it)
        }
        )
    }

    private fun getSecondCategory(position: Int) {
        this.positionSub = position
        if (!viewModel.exitToSubCategory) {
            subCategory = viewModel.categorySelected[position]
            viewModel.localGetSubCategory(subCategory)
            binding.editCategory.text = subCategory
        } else {
            viewModel.localGetSubCategory(subCategory)
            binding.editCategory.text = subCategory
            subCategory = " "
        }
        binding.textCategory.text = "Sub Category: "
    }

    private fun getThirdCategory(position: Int) {
        this.positionSubSub = position
        if (!viewModel.exitToSubSubCategory) {
            subSubCategory = viewModel.secondCategorySelected[position]
            viewModel.localGetSubSubCategory(subSubCategory)
            binding.editCategory.text = subSubCategory
        } else {
            viewModel.localGetSubSubCategory(subSubCategory)
            binding.editCategory.text = subSubCategory
            subSubCategory = " "
        }
        binding.editCategory.text = subSubCategory
        binding.textCategory.text = "SubSub Category: "
    }

    private fun observeRemoteChangesAndCompareRemoteToLocal() {
        documentsLiveData.observe(viewLifecycleOwner, {
            viewModel.compareRemoteAndLocalData(it)
        })
    }


    private fun navigateToEquipmentList(position: Int) {
        val equipmentCategory = viewModel.categorySelected[position]
        val action = CategoryFragmentDirections.actionCategoryFragmentToEquipmentListFragment(
            equipmentCategory
        )
        findNavController().navigate(action)
    }

    override fun onPause() {
        super.onPause()
        viewModel.exitToMainCategory = false
        viewModel.exitToSubCategory = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}